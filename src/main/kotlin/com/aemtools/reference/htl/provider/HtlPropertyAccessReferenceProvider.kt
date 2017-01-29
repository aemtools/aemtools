package com.aemtools.reference.htl.provider

import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlPropertyAccessReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        val propertyAccess = element as? PropertyAccessMixin ?: return arrayOf()

        val chain = propertyAccess.accessChain() ?: return arrayOf()

        val chainSegment = chain.callChainSegments.lastOrNull() ?: return arrayOf()

        val references: List<PsiReference> = chainSegment.chainElements().flatMap {
            val type = it.type
            if (type is JavaPsiClassTypeDescriptor) {
                val member = type.psiMember ?: return@flatMap listOf<PsiReference>()

                val reference = // JavaLangClassMemberReference()
                        object : PsiReferenceBase<PsiElement>(it.element, it.element.textRange, true) {
                            override fun getVariants(): Array<out Any> {
                                return arrayOf(LookupElementBuilder.create("hello"))
                            }

                            override fun resolve(): PsiElement? {
                                return member
                            }
                        }
                return@flatMap listOf(reference)
            }

            return@flatMap listOf<PsiReference>()
        }

        return references.toTypedArray()
    }
}