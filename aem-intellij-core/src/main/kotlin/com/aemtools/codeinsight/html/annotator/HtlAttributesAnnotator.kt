package com.aemtools.codeinsight.html.annotator

import com.aemtools.common.constant.const.htl.DATA_SLY_LIST
import com.aemtools.common.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.common.constant.const.htl.DATA_SLY_SET
import com.aemtools.common.constant.const.htl.DATA_SLY_TEST
import com.aemtools.common.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.common.constant.const.htl.DATA_SLY_USE
import com.aemtools.common.util.createInfoAnnotation
import com.aemtools.common.util.incomingReferences
import com.aemtools.common.util.nameRange
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_ATTRIBUTE
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_VARIABLE_DECLARATION
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_VARIABLE_UNUSED
import com.aemtools.lang.util.htlAttributeName
import com.aemtools.lang.util.htlVariableName
import com.aemtools.lang.util.isHtlFile
import com.aemtools.reference.htl.reference.HtlDeclarationReference
import com.aemtools.reference.htl.reference.HtlListHelperReference
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
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
 * @author Dmytro Primshyts
 */
class HtlAttributesAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element is XmlAttribute && element.containingFile.isHtlFile()) {
      val attributeName = element.htlAttributeName()
      val variableName = element.htlVariableName()

      if (attributeName != null) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(HTL_ATTRIBUTE)
            .range(TextRange.create(
                element.nameRange().startOffset,
                element.nameRange().startOffset + attributeName.length
            ))
            .create()
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
      DATA_SLY_SET,
      DATA_SLY_UNWRAP,
      DATA_SLY_TEST -> annotateSingleVariable(range, attribute, variableName, holder)
      DATA_SLY_LIST,
      DATA_SLY_REPEAT -> annotateIterable(range, attribute, variableName, holder)
      else -> holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(range).textAttributes(HTL_VARIABLE_DECLARATION)
          .create()
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
      holder.createInfoAnnotation(range, HTL_VARIABLE_DECLARATION)
    } else {
      holder.newSilentAnnotation(HighlightSeverity.WARNING)
          .range(range)
          .textAttributes(HTL_VARIABLE_UNUSED)
          .tooltip("Variable '$variableName' is never used.")
          .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
          .withFix(RemoveUnusedVariableFix(range))
          .create()
    }
  }

  private fun annotateSingleVariable(range: TextRange,
                                     attribute: XmlAttribute,
                                     variableName: String,
                                     holder: AnnotationHolder) {
    val references = attribute.incomingReferences()

    if (references.any { it is HtlDeclarationReference }) {
      holder.createInfoAnnotation(range, HTL_VARIABLE_DECLARATION)
    } else {
      holder.newSilentAnnotation(HighlightSeverity.WARNING)
          .textAttributes(HTL_VARIABLE_UNUSED)
          .range(range)
          .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
          .tooltip("Variable '$variableName' is never used.")
          .withFix(RemoveUnusedVariableFix(range))
          .create()
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
