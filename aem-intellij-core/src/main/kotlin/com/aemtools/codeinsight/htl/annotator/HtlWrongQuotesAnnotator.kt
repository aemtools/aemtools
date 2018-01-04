package com.aemtools.codeinsight.htl.annotator

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.aemtools.common.util.isDoubleQuoted
import com.aemtools.common.util.psiDocumentManager
import com.aemtools.common.util.toSmartPointer
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.lang.util.containerAttribute
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer

/**
 * Annotates incorrectly quoted HTL string literals with errors. e.g.:
 *
 * ```
 * <div attribute="${"it's an error"}"></div>
 * ```
 *
 * @author Dmytro Troynikov
 */
class HtlWrongQuotesAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val literal = element as? HtlStringLiteralMixin
        ?: return

    val parentAttribute = literal.containerAttribute()
        ?: return

    when {
      literal.isDoubleQuoted()
          && parentAttribute.isDoubleQuoted() -> {
        holder.createErrorAnnotation(
            literal, "Incorrect quotes"
        ).registerFix(HtlWrongQuotesLiteralFixIntentionAction(literal.toSmartPointer()))
      }

      !literal.isDoubleQuoted()
          && !parentAttribute.isDoubleQuoted() -> {
        holder.createErrorAnnotation(
            literal, "Incorrect quotes"
        ).registerFix(HtlWrongQuotesLiteralFixIntentionAction(literal.toSmartPointer()))
      }
    }

  }

}

class HtlWrongQuotesLiteralFixIntentionAction(
    private val pointer: SmartPsiElementPointer<HtlStringLiteralMixin>
) : BaseHtlIntentionAction(
    text = { "Switch HTL literal to opposite quotes" }
) {

  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val element = pointer.element ?: return
    val document = project.psiDocumentManager().getDocument(file)
        ?: return

    val newValue = element.swapQuotes()

    val (start, end) = element.textRange.startOffset to element.textRange.endOffset

    document.replaceString(start, end, newValue)
    project.psiDocumentManager().commitDocument(document)
  }

}
