package com.aemtools.reference.htl.provider

import com.aemtools.analysis.htl.callchain.elements.BaseCallChainSegment
import com.aemtools.analysis.htl.callchain.elements.BaseChainElement
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.JavaPsiUnresolvedTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TemplateParameterTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.hasChild
import com.aemtools.lang.htl.psi.HtlArrayLikeAccess
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.mixin.AccessIdentifierMixin
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.intellij.ide.util.PsiNavigationSupport
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext
import java.util.*

/**
 * @author Dmytro Troynikov
 */
object HtlPropertyAccessReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        val propertyAccess = element as? PropertyAccessMixin ?: return arrayOf()

        val chain = propertyAccess.accessChain() ?: return arrayOf()

        val chainSegment = chain.callChainSegments.lastOrNull() as? BaseCallChainSegment ?: return arrayOf()

        val elements = LinkedList<CallChainElement>(chainSegment.chainElements())

        val firstElement = elements.firstOrNull() ?: return arrayOf()
        elements.pop()

        val firstReference = extractFirstReference(chainSegment, firstElement, propertyAccess)

        val references: List<PsiReference> = elements.flatMap {
            val type = it.type
            val member = when (type) {
                is JavaPsiClassTypeDescriptor -> type.psiMember
                is JavaPsiUnresolvedTypeDescriptor -> type.psiMember
                else -> {
                    return@flatMap listOf<PsiReference>()
                }
            }

            val reference = PsiReferenceBase.Immediate(
                    propertyAccess,
                    extractTextRange(it.element),
                    true,
                    member)

            return@flatMap listOf(reference)
        }
        val refs = LinkedList(references)
        refs.addFirst(firstReference)
        return refs.toTypedArray()
    }

    private fun extractFirstReference(chainSegment: BaseCallChainSegment,
                                      firstElement: CallChainElement,
                                      propertyAccess: PropertyAccessMixin): PsiReferenceBase<PsiElement> {
        return when {
            firstElement.type is TemplateParameterTypeDescriptor -> {
                HtlTemplateParameterReference(firstElement.type as TemplateParameterTypeDescriptor,
                        propertyAccess,
                        TextRange(firstElement.element.startOffsetInParent, firstElement.element.startOffsetInParent + firstElement.element.textLength))
            }
            else -> {
                HtlDeclarationReference(chainSegment.declaration?.xmlAttribute,
                        firstElement as? BaseChainElement,
                        propertyAccess,
                        TextRange(firstElement.element.startOffsetInParent, firstElement.element.startOffsetInParent + firstElement.element.textLength))
            }
        }
    }

    private fun extractTextRange(element: PsiElement): TextRange {
        return when (element) {
            is AccessIdentifierMixin ->
                if (element.hasChild(HtlArrayLikeAccess::class.java)
                        && element.hasChild(HtlStringLiteral::class.java)) {
                    val stringLiteral: PsiElement = element.findChildrenByType(HtlStringLiteral::class.java).firstOrNull() as? PsiElement
                            ?: return TextRange.EMPTY_RANGE

                    val offset = element.startOffsetInParent + stringLiteral.startOffsetInParent + 1

                    TextRange(offset, offset + stringLiteral.text.length - 2)

                } else if (!element.hasChild(HtlArrayLikeAccess::class.java)) {
                    TextRange(element.startOffsetInParent + 1, element.startOffsetInParent + element.variableName().length + 1)
                } else {
                    TextRange.EMPTY_RANGE
                }

            is VariableNameMixin -> TextRange(element.startOffsetInParent + 1, element.startOffsetInParent + element.variableName().length + 1)
            else -> TextRange.EMPTY_RANGE
        }
    }

    class HtlTemplateParameterReference(
            val type: TemplateParameterTypeDescriptor,
            holder: PsiElement,
            range: TextRange
    ) : PsiReferenceBase<PsiElement>(holder, range, true) {
        override fun resolve(): PsiElement? =
            type.declaration.htlVariableNameElement

        override fun getVariants(): Array<Any> = emptyArray()

    }

    class HtlDeclarationReference(
            val xmlAttribute: XmlAttribute?,
            val callChainElement: BaseChainElement?,
            holder: PsiElement,
            range: TextRange)
        : PsiReferenceBase<PsiElement>(holder, range, true) {
        override fun resolve(): PsiElement? {
            val psiClass = callChainElement?.type?.asResolutionResult()?.psiClass
            if (xmlAttribute != null) {
                return HtlDeclarationIdentifier(xmlAttribute)
            } else if (psiClass != null) {
                return psiClass
            } else {
                return null
            }
        }

        override fun getVariants(): Array<Any> = emptyArray()
    }

    class HtlDeclarationIdentifier(val xmlAttribute: XmlAttribute) : FakePsiElement() {
        override fun navigate(requestFocus: Boolean) {
            val project = xmlAttribute.project
            val virtualFile = xmlAttribute.containingFile.virtualFile

            var offsetInFile = 0
            var currentElement: PsiElement = xmlAttribute
            while (currentElement != null
                    && currentElement.parent !is PsiFile) {
                offsetInFile += currentElement.startOffsetInParent
                currentElement = currentElement.parent
            }

            offsetInFile += xmlAttribute.name.indexOf(".") + 1

            PsiNavigationSupport.getInstance()
                    .createNavigatable(project, virtualFile, offsetInFile)
                    .navigate(requestFocus)
        }

        override fun getText(): String? {
            return xmlAttribute.value
        }

        override fun getParent(): PsiElement {
            return xmlAttribute
        }

        override fun getName(): String? {
            return super.getName()
        }

        override fun toString(): String {
            return xmlAttribute.text
        }

    }

}