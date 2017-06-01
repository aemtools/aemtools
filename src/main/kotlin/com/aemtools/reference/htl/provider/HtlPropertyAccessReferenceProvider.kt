package com.aemtools.reference.htl.provider

import com.aemtools.analysis.htl.callchain.elements.BaseCallChainSegment
import com.aemtools.analysis.htl.callchain.elements.BaseChainElement
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateParameterTypeDescriptor
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.hasChild
import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.icons.HtlIcons
import com.aemtools.lang.htl.psi.HtlArrayLikeAccess
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.htl.psi.mixin.AccessIdentifierMixin
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.reference.common.reference.HtlPropertyAccessReference
import com.intellij.lang.Language
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext
import java.util.*
import javax.swing.Icon

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
            val actualReferenceHolder = it.element
            val type = it.type
            val referencedElement = type.referencedElement()
                    ?: return@flatMap emptyList<PsiReference>()

            val reference = HtlPropertyAccessReference(
                    propertyAccess,
                    it,
                    extractTextRange(actualReferenceHolder),
                    referencedElement
            )

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

        override fun handleElementRename(newElementName: String?): PsiElement {
            return super.handleElementRename(newElementName)
        }

//        override fun isReferenceTo(element: PsiElement?): Boolean {
//            return xmlAttribute?.manager?.areElementsEquivalent(xmlAttribute, element)
//                    ?: false
//        }

        override fun getVariants(): Array<Any> = emptyArray()
    }

    class HtlDeclarationIdentifier(val xmlAttribute: XmlAttribute)
        : FakePsiElement(), PsiNamedElement {

        override fun getText(): String? {
            return xmlAttribute.name.substringAfter(".", "item")
        }

        override fun getParent(): PsiElement {
            return xmlAttribute
        }

        override fun getName(): String? {
            val variableName = if (xmlAttribute.name.contains(".")) {
                xmlAttribute.name.substringAfter(".")
            } else {
                xmlAttribute.name
            }

            return variableName
        }

        override fun setName(name: String): PsiElement {
            val attributeName = if (xmlAttribute.name.contains(".")) {
                xmlAttribute.name.substringBefore(".")
            } else {
                xmlAttribute.name
            }

            return xmlAttribute.setName("$attributeName.$name")
        }

        override fun toString(): String {
            return xmlAttribute.text
        }

        override fun getTextRange(): TextRange? {
            return if (xmlAttribute.name.contains(".")) {
                val start = xmlAttribute.textRange.startOffset + xmlAttribute.name.indexOf(".") + 1
                TextRange(start, start + (name?.length ?: xmlAttribute.nameElement.textLength))
            } else {
                xmlAttribute.nameElement.textRange
            }
        }

        override fun getTextOffset(): Int {
            return if (xmlAttribute.name.contains(".")) {
                xmlAttribute.nameElement.textOffset + xmlAttribute.name.indexOf(".") + 1
            } else {
                xmlAttribute.nameElement.textOffset
            }
        }

        override fun getStartOffsetInParent(): Int {
            return if (xmlAttribute.name.contains(".")) {
                xmlAttribute.startOffsetInParent + xmlAttribute.name.indexOf(".") + 1
            } else {
                xmlAttribute.startOffsetInParent
            }
        }

        override fun getTextLength(): Int {
            return name?.length ?: 0
        }

        override fun textToCharArray(): CharArray {
            return text?.toCharArray() ?: kotlin.CharArray(0)
        }

        override fun textContains(c: Char): Boolean {
            return textToCharArray().contains(c)
        }

        override fun getPresentableText(): String? {
            return "HTL Variable"
        }

        override fun getLocationString(): String? {
            return xmlAttribute.containingFile.name
        }

        override fun getIcon(open: Boolean): Icon? {
            return HtlIcons.SLY_USE_VARIABLE_ICON
        }

        override fun isPhysical(): Boolean = true

        override fun getLanguage(): Language = HtlLanguage

        override fun getContainingFile(): PsiFile {
            return xmlAttribute.containingFile
        }

        override fun equals(other: Any?): Boolean {
            return when (other) {
                is HtlDeclarationIdentifier -> other.xmlAttribute == xmlAttribute
                is XmlAttribute -> xmlAttribute == other
                else -> false
            }
        }

        override fun hashCode(): Int {
            return toString().hashCode()
        }
    }

}