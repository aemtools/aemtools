package com.aemtools.completion.htl.callchain.typedescriptor.java

import com.aemtools.completion.htl.callchain.typedescriptor.TypeDescriptor
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
open class JavaPsiClassTypeDescriptor(val psiClass: PsiClass, private val isArray: Boolean = false) : TypeDescriptor {
    override fun isArray(): Boolean = isArray

    override fun isList(): Boolean {
        val listClass = JavaSearch.findClass("java.util.List", psiClass.project) ?: return false

        return this.psiClass.isInheritor(listClass, true)
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
            this is PsiClassReferenceType -> this.resolve()?.qualifiedName
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

}