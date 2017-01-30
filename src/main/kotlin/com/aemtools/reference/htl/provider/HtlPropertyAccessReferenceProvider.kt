package com.aemtools.reference.htl.provider

import com.aemtools.analysis.htl.callchain.elements.BaseCallChainSegment
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.lang.htl.psi.mixin.AccessIdentifierMixin
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import java.util.*

/**
 * @author Dmytro Troynikov
 */
object HtlPropertyAccessReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        val propertyAccess = element as? PropertyAccessMixin ?: return arrayOf()

        val chain = propertyAccess.accessChain() ?: return arrayOf()

        val chainSegment = chain.callChainSegments.lastOrNull() as BaseCallChainSegment ?: return arrayOf()

        val elements = LinkedList<CallChainElement>(chainSegment.chainElements())

        val firstElement = elements.firstOrNull() ?: return arrayOf()
        elements.pop()

        val firstReference = object : PsiReferenceBase<PsiElement>(firstElement.element,
                TextRange(0, firstElement.element.textLength), true) {
            override fun resolve(): PsiElement? {
                return chainSegment.declaration?.xmlAttribute?.valueElement
            }

            override fun getVariants(): Array<out Any> {
                return arrayOf()
            }
        }

        val references: List<PsiReference> = chainSegment.chainElements().flatMap {
            val type = it.type
            if (type is JavaPsiClassTypeDescriptor) {
                val member = type.psiMember ?: return@flatMap listOf<PsiReference>()

                val reference =
                        object : PsiReferenceBase<PsiElement>(propertyAccess, extractTextRange(it.element),
                                true) {
                            override fun getVariants(): Array<out Any> {
                                return arrayOf()
                            }

                            override fun resolve(): PsiElement? {
                                return member
                            }
                        }
                return@flatMap listOf(reference)
            }

            return@flatMap listOf<PsiReference>()
        }
        val refs = LinkedList(references)
        refs.addFirst(firstReference)
        return refs.toTypedArray()
    }

    private fun extractTextRange(element: PsiElement): TextRange {
        return when (element) {
            is AccessIdentifierMixin -> TextRange(element.startOffsetInParent + 1, element.startOffsetInParent + element.variableName().length + 1)
            is VariableNameMixin -> TextRange(element.startOffsetInParent + 1, element.startOffsetInParent + element.variableName().length + 1)
            else -> TextRange.EMPTY_RANGE
        }
    }
}