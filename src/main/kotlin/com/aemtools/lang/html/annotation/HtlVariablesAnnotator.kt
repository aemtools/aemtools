package com.aemtools.lang.html.annotation

import com.aemtools.completion.util.isHtlDeclarationAttribute
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlVariablesAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is XmlAttribute
                && element.isHtlDeclarationAttribute()
                && element.hasVariable()) {
            val range = TextRange(element.nameElement.textRange.startOffset + element.name.indexOf(".") + 1,
                    element.nameElement.textRange.endOffset)

            holder.createInfoAnnotation(range, null).textAttributes =
                    TextAttributesKey.createTextAttributesKey("BLOCK_VARIABLE", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        }
    }

    private fun XmlAttribute.hasVariable() = name.contains(".") && name.indexOf(".") < name.length

}