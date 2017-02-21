package com.aemtools.lang.html.annotation

import com.aemtools.completion.util.getHtlFile
import com.aemtools.completion.util.htlAttributeName
import com.aemtools.completion.util.htlVariableName
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlVariablesAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is XmlAttribute) {
            val attributeName = element.htlAttributeName()
            val variableName = element.htlVariableName()
            if (attributeName != null && variableName != null) {

                val range = TextRange(element.nameElement.textRange.startOffset + element.name.indexOf(".") + 1,
                        element.nameElement.textRange.endOffset)

                annotate(range, element, attributeName, variableName, holder)
            }
        }
    }

    private fun annotate(range: TextRange,
                         attribute: XmlAttribute,
                         attributeName: String,
                         variableName: String,
                         holder: AnnotationHolder): Unit {
        when {
            attributeName == DATA_SLY_USE -> annotateDataSlyUse(range, attribute, variableName, holder)
            else -> holder.createInfoAnnotation(range, null).textAttributes = HTL_VARIABLE
        }

    }

    private fun annotateDataSlyUse(range: TextRange,
                                   attribute: XmlAttribute,
                                   variableName: String,
                                   holder: AnnotationHolder) {
        val htlFile = attribute.containingFile.getHtlFile()
                ?: return

//        val pas = htlFile.findChildrenByClass(PropertyAccessMixin::class.java)
//                .find {
//                    it.callChain()
//                            .first
//                            .myDeclaration
//                            ?.xmlAttribute
//                            ?.isEquivalentTo(attribute)
//                        ?: false
//                }

        val annotation = holder.createInfoAnnotation(range, null)
        annotation.textAttributes = HTL_VARIABLE
        // todo add "unused" version
//        if (pas != null ) {
//        } else {
//            annotation.textAttributes = HTL_VARIABLE_UNUSED
//            annotation.highlightType = ProblemHighlightType.LIKE_UNUSED_SYMBOL
//            annotation.registerFix(RemoveUnusedVariableFix(range))
//        }
    }

    private class RemoveUnusedVariableFix(val range: TextRange) : IntentionAction {
        override fun getFamilyName(): String = "Htl"
        override fun startInWriteAction(): Boolean = true

        override fun getText(): String = "Remove unused variable."

        override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

        override fun invoke(project: Project, editor: Editor?, file: PsiFile) {
            val document = PsiDocumentManager.getInstance(project)
                    .getDocument(file)
                    ?: return
            document.replaceString(range.startOffset - 1, range.endOffset, "")
        }

    }

    companion object {
        val HTL_VARIABLE = TextAttributesKey.createTextAttributesKey("HTL_VARIABLE", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val HTL_VARIABLE_UNUSED = TextAttributesKey.createTextAttributesKey("HTL_VARIABLE_UNUSED", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE)
    }

}