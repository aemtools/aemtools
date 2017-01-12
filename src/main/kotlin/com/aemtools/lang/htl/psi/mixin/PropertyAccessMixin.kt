package com.aemtools.lang.htl.psi.mixin

import com.aemtools.completion.htl.completionprovider.FileVariablesResolver
import com.aemtools.completion.htl.model.HtlVariableDeclaration
import com.aemtools.completion.htl.model.PropertyAccessChainUnit
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.util.extractHtlHel
import com.aemtools.completion.util.extractPropertyAccess
import com.aemtools.completion.util.resolveUseClass
import com.aemtools.lang.htl.psi.util.byNormalizedName
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
        var firstChainElement = callChain.pop()

        var (names, propertyAccessChain, resolutionResult)
                = resolveFirstItem(firstChainElement)

        // unable to resolve root element, no need to proceed with resolution
        if (resolutionResult.isEmpty()) {
            return propertyAccessChain
        }

        var previousDeclaration: HtlVariableDeclaration? = null
        do {
            val subChainResolution = resolveSubChain(firstChainElement,
                    resolutionResult, previousDeclaration)

            propertyAccessChain.addAll(subChainResolution)

            if (callChain.isNotEmpty()) {
                previousDeclaration = firstChainElement.myDeclaration
                firstChainElement = callChain.pop()
            } else {
                break
            }

        } while (true)

        return propertyAccessChain
    }

    private fun resolveSubChain(callChain: CallChainUnit,
                                previousResolutionResult: ResolutionResult,
                                previousDeclaration: HtlVariableDeclaration?): LinkedList<PropertyAccessChainUnit> {
        val elements = LinkedList(callChain.myCallChain)
        val result = LinkedList<PropertyAccessChainUnit>()
        var resolutionResult: ResolutionResult = previousResolutionResult
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

    private fun resolveFirstItem(firstChainElement: CallChainUnit): Triple<LinkedList<PsiElement>, LinkedList<PropertyAccessChainUnit>, ResolutionResult> {
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

        val firstElement = names.pop() as VariableNameMixin
        var resolutionResult = firstElement.resolve()

        if (resolutionResult.predefined != null) {
            propertyAccessChain.add(PropertyAccessChainUnit(firstElement.variableName(),
                    firstElement.variableName(), resolutionResult.psiClass?.qualifiedName, resolutionResult, null))
        } else {
            propertyAccessChain.add(PropertyAccessChainUnit(firstElement.variableName(),
                    firstElement.variableName(), resolutionResult.psiClass?.qualifiedName, ResolutionResult(resolutionResult.psiClass), null))
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
    private fun callChain(): LinkedList<CallChainUnit> {
        var result = LinkedList<CallChainUnit>()
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

        val myChainUnit = CallChainUnit(myChain, declaration)

        return LinkedList(listOf(*result.toTypedArray(), myChainUnit))
    }

    private fun createUseChainUnit(declaration: HtlVariableDeclaration, useClass: String): LinkedList<CallChainUnit> {
        val result = LinkedList<CallChainUnit>()

        val jpc = JavaPsiFacade.getInstance(project)
        val clazz = jpc.findClass(useClass, GlobalSearchScope.allScope(project))

        result.add(CallChainUnit(LinkedList(), HtlVariableDeclaration(
                declaration.xmlAttribute,
                declaration.variableName,
                declaration.type,
                ResolutionResult(clazz)
        )))
        return result
    }

    private data class CallChainUnit(val myCallChain: LinkedList<PsiElement>,
                                     val myDeclaration: HtlVariableDeclaration? = null)

}
