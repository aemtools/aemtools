package com.aemtools.lang.html.annotation

import com.aemtools.completion.util.htlAttributeName
import com.aemtools.completion.util.htlVariableName
import com.aemtools.completion.util.incomingReferences
import com.aemtools.completion.util.nameRange
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.lang.common.highlight
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_ATTRIBUTE
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_VARIABLE_DECLARATION
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_VARIABLE_UNUSED
import com.aemtools.reference.htl.reference.HtlDeclarationReference
import com.aemtools.reference.htl.reference.HtlListHelperReference
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute

/**
 * Annotates Htl attributes
 *
 * @author Dmytro Troynikov
 */
class HtlAttributesAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element is XmlAttribute) {
      val attributeName = element.htlAttributeName()
      val variableName = element.htlVariableName()

      if (attributeName != null) {
        val attributeAnnotation = holder.createInfoAnnotation(
            TextRange.create(
                element.nameRange().startOffset,
                element.nameRange().startOffset + attributeName.length
            ),
            null
        )
        attributeAnnotation.textAttributes = HTL_ATTRIBUTE
      }

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
                       holder: AnnotationHolder) {
    when (attributeName) {
      DATA_SLY_USE,
      DATA_SLY_TEST -> annotateSingleVariable(range, attribute, variableName, holder)
      DATA_SLY_LIST,
      DATA_SLY_REPEAT -> annotateIterable(range, attribute, variableName, holder)
      else -> holder.createInfoAnnotation(range, null).textAttributes = HTL_VARIABLE_DECLARATION
    }
  }

  private fun annotateIterable(range: TextRange,
                               attribute: XmlAttribute,
                               variableName: String,
                               holder: AnnotationHolder) {
    val references = attribute.incomingReferences()

    if (references.any {
      it is HtlDeclarationReference
          || it is HtlListHelperReference
    }) {
      holder.highlight(range, HTL_VARIABLE_DECLARATION)
    } else {
      val annotation = holder.createWarningAnnotation(range, null)
      annotation.textAttributes = HTL_VARIABLE_UNUSED
      annotation.highlightType = ProblemHighlightType.LIKE_UNUSED_SYMBOL
      annotation.tooltip = "Variable '$variableName' is never used."
      annotation.registerFix(RemoveUnusedVariableFix(range))
    }
  }

  private fun annotateSingleVariable(range: TextRange,
                                     attribute: XmlAttribute,
                                     variableName: String,
                                     holder: AnnotationHolder) {
    val references = attribute.incomingReferences()

    if (references.any { it is HtlDeclarationReference }) {
      holder.highlight(range, HTL_VARIABLE_DECLARATION)
    } else {
      val annotation = holder.createWarningAnnotation(range, null)
      annotation.textAttributes = HTL_VARIABLE_UNUSED
      annotation.highlightType = ProblemHighlightType.LIKE_UNUSED_SYMBOL
      annotation.tooltip = "Variable '$variableName' is never used."
      annotation.registerFix(RemoveUnusedVariableFix(range))
    }
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

}
