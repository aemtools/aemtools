package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.lang.java.JavaSearch
import com.aemtools.util.allScope
import com.aemtools.util.psiManager
import com.aemtools.util.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import java.util.*

/**
 * Type descriptor which uses given [PsiClass] to provide type information.
 *
 * @author Dmytro_Troynikov
 */
open class JavaPsiClassTypeDescriptor(open val psiClass: PsiClass,
                                      open val psiMember: PsiMember? = null,
                                      open val originalType: PsiType? = null) : TypeDescriptor {
    override fun isArray(): Boolean = originalType is PsiArrayType

    override fun isIterable(): Boolean {
        val iterableInterface = JavaSearch.findClass("java.lang.Iterable", psiClass.project) ?: return false

        return this.psiClass.isInheritor(iterableInterface, true)
                || this.psiClass.isEquivalentTo(iterableInterface)
    }

    override fun isMap(): Boolean {
        val mapClass = JavaSearch.findClass("java.util.Map", psiClass.project) ?: return false

        return this.psiClass.isInheritor(mapClass, true)
                || this.psiClass.isEquivalentTo(mapClass)
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

            result.add(lookupElement.withPriority(extractPriority(it, psiClass)))
        }
        fields.forEach {
            val lookupElement = LookupElementBuilder.create(it.name.toString())
                    .withIcon(it.getIcon(0))
                    .withTypeText(it.type.presentableText, true)

            result.add(lookupElement.withPriority(extractPriority(it, psiClass)))
        }

        return result
    }

    private fun extractPriority(member: PsiMember, clazz: PsiMember) = when {
        member.containingClass == clazz -> 1.0
        member.containingClass?.qualifiedName == "java.lang.Object" -> 0.0
        else -> 0.5
    }

    override fun name(): String = psiClass.qualifiedName
            ?: psiClass.name
            ?: psiClass.text

    override fun subtype(identifier: String): TypeDescriptor {
        val psiMember = psiClass.findElMemberByName(identifier)
                ?: return TypeDescriptor.empty()

        val psiType = psiMember.resolveReturnType()
                ?: return TypeDescriptor.empty()

        val className = with(psiType) {
            when {
                this is PsiClassReferenceType -> {
                    this.rawType().canonicalText
                }
                this is PsiClassType -> this.className
                this is PsiPrimitiveType -> this.getBoxedType(
                        psiClass.project.psiManager(),
                        psiClass.project.allScope()
                )?.canonicalText
                this is PsiArrayType -> {
                    this.componentType.canonicalText
                }
                else -> null
            }
        } ?: return TypeDescriptor.empty()

        val typeClass = JavaSearch.findClass(className, psiClass.project)
                ?: return TypeDescriptor.named(className, psiMember, psiType)

        return JavaPsiClassTypeDescriptor.create(typeClass, psiMember, psiType)
    }

    override fun referencedElement(): PsiElement? =
            psiMember

    override fun asResolutionResult(): ResolutionResult =
            ResolutionResult(psiClass, myVariants())

    companion object {
        fun create(psiClass: PsiClass,
                   psiMember: PsiMember? = null,
                   psiType: PsiType? = null): JavaPsiClassTypeDescriptor {
            return when (psiType) {
                is PsiClassReferenceType -> {
                    val iterable = JavaSearch.findClass("java.lang.Iterable", psiClass.project)
                    val iterator = JavaSearch.findClass("java.util.Iterator", psiClass.project)
                    val map = JavaSearch.findClass("java.util.Map", psiClass.project)

                    if (iterable != null && map != null && iterator != null) {
                        if (psiClass.isInheritor(iterable, true)
                                || psiClass.isEquivalentTo(iterable)) {
                            return IterableJavaTypeDescriptor(psiClass, psiMember, psiType)
                        }

                        if (psiClass.isInheritor(iterator, true)
                                || psiClass.isEquivalentTo(iterator)) {
                            return IterableJavaTypeDescriptor(psiClass, psiMember, psiType)
                        }

                        if (psiClass.isInheritor(map, true)
                                || psiClass.isEquivalentTo(map)) {
                            return MapJavaTypeDescriptor(psiClass, psiMember, psiType)
                        }

                    }

                    JavaPsiClassTypeDescriptor(psiClass, psiMember, psiType)
                }
                is PsiClassType,
                is PsiPrimitiveType ->
                    JavaPsiClassTypeDescriptor(psiClass, psiMember, psiType)
                is PsiArrayType ->
                    ArrayJavaTypeDescriptor(psiClass, psiMember, psiType)
                else -> JavaPsiClassTypeDescriptor(psiClass, psiMember, psiType)
            }
        }
    }

}
