package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.lang.htl.psi.util.*
import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.search.GlobalSearchScope
import java.util.*

/**
 * Type descriptor which uses given [PsiClass] to provide type information.
 * @author Dmytro_Troynikov
 */
open class JavaPsiClassTypeDescriptor(open val psiClass: PsiClass,
                                      // it's not possible to check if current [PsiClass] is an array.
                                      private val isArray: Boolean = false) : TypeDescriptor {
    override fun isArray(): Boolean = isArray

    override fun isIterable(): Boolean {
        val iterableInterface = JavaSearch.findClass("java.lang.Iterable", psiClass.project) ?: return false

        return this.psiClass.isInheritor(iterableInterface, true)
    }

    override fun isMap(): Boolean {
        val mapClass = JavaSearch.findClass("java.util.Map", psiClass.project) ?: return false

        return this.psiClass.isInheritor(mapClass, true)
    }

    override fun myVariants(): List<LookupElement> {
        val methods = psiClass.elMethods()
        val fields = psiClass.elFields()

        val methodNames = ArrayList<String>()
        val result = ArrayList<LookupElement>()

        methods.forEach {
            var name = it.elName()
            if (methodNames.contains(name)) {
                name = it.name
            } else {
                methodNames.add(name)
            }
            var lookupElement = LookupElementBuilder.create(name)
                    .withIcon(it.getIcon(0))
                    .withTailText(" ${it.name}()", true)

            val returnType = it.returnType
            if (returnType != null) {
                lookupElement = lookupElement.withTypeText(returnType.presentableText, true)
            }

            result.add(lookupElement)
        }

        fields.forEach {
            val lookupElement = LookupElementBuilder.create(it.name.toString())
                    .withIcon(it.getIcon(0))
                    .withTypeText(it.type.presentableText, true)

            result.add(lookupElement)
        }

        return result
    }

    override fun name(): String = psiClass.qualifiedName
            ?: psiClass.name
            ?: psiClass.text

    override fun subtype(identifier: String): TypeDescriptor {
        val psiMember = psiClass.findElMemberByName(identifier)
                ?: return TypeDescriptor.empty()

        val psiType = psiMember.resolveReturnType()
                ?: return TypeDescriptor.empty()

        val className = with(psiType) { when {
            this is PsiClassReferenceType -> {
                this.resolve()?.qualifiedName
                        ?: return JavaPsiClassReferenceTypeDescriptor(psiType, psiClass.project)
            }
            this is PsiClassType -> this.className
            this is PsiPrimitiveType -> this.getBoxedType(
                    PsiManager.getInstance(psiClass.project),
                    GlobalSearchScope.allScope(psiClass.project)
            )?.className
            this is PsiArrayType -> this.canonicalText
            else -> null
        } } ?: return TypeDescriptor.empty()

        val typeClass = JavaSearch.findClass(className, psiClass.project)
            ?: return TypeDescriptor.named(className)

        return JavaPsiClassTypeDescriptor(typeClass)
    }

    override fun asResolutionResult(): ResolutionResult {
        return ResolutionResult(psiClass, myVariants())
    }

}