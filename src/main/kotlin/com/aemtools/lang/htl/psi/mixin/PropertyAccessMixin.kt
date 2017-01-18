package com.aemtools.lang.htl.psi.mixin

import com.aemtools.completion.htl.callchain.HtlCallChainResolver
import com.aemtools.completion.htl.completionprovider.FileVariablesResolver
import com.aemtools.completion.htl.model.HtlVariableDeclaration
import com.aemtools.completion.htl.model.PropertyAccessChainUnit
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.util.extractHtlHel
import com.aemtools.completion.util.extractPropertyAccess
import com.aemtools.completion.util.resolveUseClass
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.util.byNormalizedName
import com.aemtools.lang.htl.psi.util.resolveClassName
import com.aemtools.lang.htl.psi.util.resolveReturnType
import com.intellij.lang.ASTNode
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMember
import com.intellij.psi.search.GlobalSearchScope
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
abstract class PropertyAccessMixin(node: ASTNode) : HtlELNavigableMixin(node) {

    fun accessChain(): List<PropertyAccessChainUnit> {
        val callChain = callChain()
        var chainElement = callChain.pop()
        val res = HtlCallChainResolver.resolveCallChain(this)
        var (names, propertyAccessChain, resolutionResult)
                = resolveFirstItem(chainElement)

        // unable to resolve root element, no need to proceed with resolution
        if (resolutionResult.isEmpty()) {
            return propertyAccessChain
        }

        while (callChain.isNotEmpty()) {
            chainElement = callChain.pop()
            val subChainResolution = resolveSubChain(chainElement,
                    resolutionResult, propertyAccessChain.last(), chainElement.myDeclaration)

            propertyAccessChain.addAll(subChainResolution)
        }

        return propertyAccessChain
    }

    private fun resolveSubChain(callChain: RawChainUnit,
                                previousResolutionResult: ResolutionResult,
                                previousChainUnit: PropertyAccessChainUnit,
                                previousDeclaration: HtlVariableDeclaration?): Deque<PropertyAccessChainUnit> {
        val elements = LinkedList(callChain.myCallChain)
        val result = LinkedList<PropertyAccessChainUnit>()
        var resolutionResult: ResolutionResult = previousResolutionResult

        if (elements.isNotEmpty()) {
            val firstElement = elements.pop()
            val variableName = extractElementName(firstElement)
            val previousMember = previousChainUnit.psiMember

            val previousPsiClass = previousResolutionResult.psiClass

            if (previousMember != null) {
                val returnType = previousMember.resolveReturnType() ?: return LinkedList()
                val previousDeclarationType = previousDeclaration?.type ?: return LinkedList()
                val className = returnType.resolveClassName(previousMember, previousDeclarationType, project)
                        ?: return LinkedList()
                val psiClass = JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project))
                        ?: return LinkedList()

                resolutionResult = ResolutionResult(psiClass)

                result.add(PropertyAccessChainUnit(
                        variableName,
                        variableName,
                        className,
                        resolutionResult,
                        previousMember
                ))
            } else if (previousPsiClass != null) {
                result.add(PropertyAccessChainUnit(variableName, variableName, previousPsiClass.qualifiedName, previousResolutionResult, null))
            } else {
                return LinkedList()
            }
        }

        while (elements.isNotEmpty() && resolutionResult.psiClass != null) {
            val nextField = elements.pop()
            val variableName = extractElementName(nextField)

            val clazz = resolutionResult.psiClass as PsiClass

            val memberAndTypeClass =
                    extractMemberAndTypeClass(clazz, previousDeclaration, variableName)

            if (memberAndTypeClass != null) {
                val (psiMember, clazzOfVariable) = memberAndTypeClass

                resolutionResult = ResolutionResult(clazzOfVariable)
                result.add(PropertyAccessChainUnit(variableName,
                        psiMember?.name, resolutionResult.psiClass?.qualifiedName, resolutionResult, psiMember))
            }

        }
        return result
    }

    private fun extractMemberAndTypeClass(clazz: PsiClass, declaration: HtlVariableDeclaration?, variableName: String)
            : Pair<PsiMember, PsiClass?>? {
        val prevDeclarationType = declaration?.type
        return if (prevDeclarationType != null) {
            clazz.byNormalizedName(variableName, project, prevDeclarationType)
        } else {
            clazz.byNormalizedName(variableName, project)
        }
    }

    private fun extractElementName(nextField: PsiElement?): String {
        return when (nextField) {
            is AccessIdentifierMixin -> nextField.variableName()
            is VariableNameMixin -> nextField.variableName()
            else -> ""
        }
    }

    private fun resolveFirstItem(firstChainElement: RawChainUnit): Triple<LinkedList<PsiElement>, LinkedList<PropertyAccessChainUnit>, ResolutionResult> {
        val propertyAccessChain = LinkedList<PropertyAccessChainUnit>()
        var names = firstChainElement.myCallChain

        if (firstChainElement.myDeclaration?.resolutionResult?.isNotEmpty() ?: false) {
            val resolutionResult = firstChainElement.myDeclaration?.resolutionResult
            val declaration = firstChainElement.myDeclaration
            if (resolutionResult != null &&
                    declaration != null) {

                propertyAccessChain.add(PropertyAccessChainUnit(declaration.variableName,
                        declaration.variableName, resolutionResult.psiClass?.qualifiedName, resolutionResult, null))

                return Triple(
                        firstChainElement.myCallChain,
                        propertyAccessChain,
                        firstChainElement.myDeclaration?.resolutionResult ?: ResolutionResult()
                )
            }
        }

        var firstElement = names.pop() as VariableNameMixin
        var resolutionResult = firstElement.resolve()

        if (resolutionResult.predefined != null) {
            propertyAccessChain.add(PropertyAccessChainUnit(firstElement.variableName(),
                    firstElement.variableName(), resolutionResult.psiClass?.qualifiedName, resolutionResult, null))
        } else {
            propertyAccessChain.add(PropertyAccessChainUnit(firstElement.variableName(),
                    firstElement.variableName(), resolutionResult.psiClass?.qualifiedName, ResolutionResult(resolutionResult.psiClass), null))
        }

        while (names.isNotEmpty()) {
            var nextItem = names.pop()
            val variableName = extractElementName(nextItem)
            val clazz = resolutionResult.psiClass ?: break

            val memberAndTypeClass =
                    extractMemberAndTypeClass(clazz, null, variableName)

            if (memberAndTypeClass != null) {
                val (psiMember, clazzOfVariable) = memberAndTypeClass

                resolutionResult = ResolutionResult(clazzOfVariable)
                propertyAccessChain.add(PropertyAccessChainUnit(variableName,
                        psiMember?.name, resolutionResult.psiClass?.qualifiedName, resolutionResult, psiMember))
            }
        }

        return Triple(names, propertyAccessChain, resolutionResult)
    }

    fun resolveIterable(): ResolutionResult {
        val names = callChain()
        val propertyAccessChain = LinkedList<PropertyAccessChainUnit>()

        val accessChain = accessChain()

        val firstElement = names.pop() as VariableNameMixin
        val resolutionResult = firstElement.resolve()

        if (resolutionResult.predefined != null) {
            propertyAccessChain.add(PropertyAccessChainUnit(firstElement.variableName(),
                    firstElement.variableName(), resolutionResult.psiClass?.qualifiedName, resolutionResult, null))
        } else {
            propertyAccessChain.add(PropertyAccessChainUnit(firstElement.variableName(),
                    firstElement.variableName(), resolutionResult.psiClass?.qualifiedName, ResolutionResult(resolutionResult.psiClass), null))
        }

        return ResolutionResult()
    }

    /**
     * Returns call chain of current element.
     */
    fun callChain(): LinkedList<RawChainUnit> {
        var result = LinkedList<RawChainUnit>()
        var myChain = LinkedList(listOf(*this.children))

        val firstElement = myChain.first() as VariableNameMixin
        val firstName = firstElement.variableName()

        val declaration = FileVariablesResolver.findDeclaration(firstName, this.containingFile)

        if (declaration != null
                && declaration.resolutionResult.isEmpty()) {
            val propertyAccessMixin = declaration.xmlAttribute.extractHtlHel()?.extractPropertyAccess()

            // if property access mixin is available recursively obtain it's call chain
            if (propertyAccessMixin != null) {
                result = propertyAccessMixin.callChain()
            }

            if (propertyAccessMixin == null) {
                val useClass = declaration.xmlAttribute.resolveUseClass()
                if (useClass != null) {
                    result = createUseChainUnit(declaration, useClass)
                }
            }
        }

        val myChainUnit = RawChainUnit(myChain, declaration)

        return LinkedList(listOf(*result.toTypedArray(), myChainUnit))
    }

    private fun createUseChainUnit(declaration: HtlVariableDeclaration, useClass: String): LinkedList<RawChainUnit> {
        val result = LinkedList<RawChainUnit>()

        val jpc = JavaPsiFacade.getInstance(project)
        val clazz = jpc.findClass(useClass, GlobalSearchScope.allScope(project))

        result.add(RawChainUnit(LinkedList(), HtlVariableDeclaration(
                declaration.xmlAttribute,
                declaration.variableName,
                declaration.type,
                ResolutionResult(clazz)
        )))
        return result
    }

}
