package com.aemtools.codeinsight.htl.annotator

import com.aemtools.common.util.isDoubleQuoted
import com.aemtools.common.util.toSmartPointer
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.lang.util.containerAttribute
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

/**
 * Annotates incorrectly quoted HTL string literals with errors. e.g.:
 *
 * ```
 * <div attribute="${"it's an error"}"></div>
 * ```
 *
 * @author Dmytro Primshyts
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
        holder.newAnnotation(HighlightSeverity.ERROR, "Incorrect quotes")
            .newFix(HtlWrongQuotesLiteralFixIntentionAction(literal.toSmartPointer()))
            .registerFix()
            .newFix(HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction(
                parentAttribute.toSmartPointer()))
            .registerFix()
            .create()
      }

      !literal.isDoubleQuoted()
          && !parentAttribute.isDoubleQuoted() -> {
        holder.newAnnotation(HighlightSeverity.ERROR, "Incorrect quotes")
            .newFix(HtlWrongQuotesLiteralFixIntentionAction(literal.toSmartPointer()))
            .registerFix()
            .newFix(HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction(
                parentAttribute.toSmartPointer()))
            .registerFix()
            .create()
      }
    }

  }

}

