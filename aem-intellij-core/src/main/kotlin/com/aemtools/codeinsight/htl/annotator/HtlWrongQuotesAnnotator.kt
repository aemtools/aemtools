package com.aemtools.codeinsight.htl.annotator

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.isDoubleQuoted
import com.aemtools.common.util.psiDocumentManager
import com.aemtools.common.util.toSmartPointer
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.lang.util.containerAttribute
import com.aemtools.lang.util.getHtlFile
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.xml.XmlAttribute

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
        ).apply {
          registerFix(HtlWrongQuotesLiteralFixIntentionAction(literal.toSmartPointer()))
          registerFix(HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction(
              parentAttribute.toSmartPointer())
          )
        }
      }

      !literal.isDoubleQuoted()
          && !parentAttribute.isDoubleQuoted() -> {
        holder.createErrorAnnotation(
            literal, "Incorrect quotes"
        ).apply {
          registerFix(HtlWrongQuotesLiteralFixIntentionAction(literal.toSmartPointer()))
          registerFix(HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction(
              parentAttribute.toSmartPointer())
          )
        }
      }
    }

  }

}

class HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction(
    private val pointer: SmartPsiElementPointer<XmlAttribute>
) : BaseHtlIntentionAction(
    text = { "Invert XML Attribute quotes" }
) {
  override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    val element = pointer.element ?: return
    val valueElement = element.valueElement ?: return
    val psiDocumentManager = project.psiDocumentManager()

    val document = psiDocumentManager.getDocument(file)
        ?: return

    val rootDoublequoted = element.isDoubleQuoted()

    val htl = element.containingFile.getHtlFile()
        ?: return

    if (rootDoublequoted) {
      document.replaceString(
          valueElement.textRange.startOffset,
          valueElement.textRange.startOffset + 1,
          "'"
      )
      document.replaceString(
          valueElement.textRange.endOffset - 1,
          valueElement.textRange.endOffset,
          "'"
      )
    } else {
      document.replaceString(
          valueElement.textRange.startOffset,
          valueElement.textRange.startOffset + 1,
          "\""
      )
      document.replaceString(
          valueElement.textRange.endOffset - 1,
          valueElement.textRange.endOffset,
          "\""
      )
    }

    val htlLiterals = element.findChildrenByType(OuterLanguageElement::class.java)
        .flatMap { outerLanguageElement ->
          val htlEl = htl.findElementAt(outerLanguageElement.textOffset)
              ?.findParentByType(HtlHtlEl::class.java)
              ?: return@flatMap emptyList<HtlStringLiteralMixin>()
          htlEl.findChildrenByType(HtlStringLiteralMixin::class.java)
        }

    htlLiterals.forEach { literal ->
      val newVal = if (rootDoublequoted) {
        literal.toDoubleQuoted()
      } else {
        literal.toSingleQuoted()
      }
      document.replaceString(
          literal.textRange.startOffset,
          literal.textRange.endOffset,
          newVal
      )
    }

    psiDocumentManager.commitDocument(document)
  }
}

class HtlWrongQuotesLiteralFixIntentionAction(
    private val pointer: SmartPsiElementPointer<HtlStringLiteralMixin>
) : BaseHtlIntentionAction(
    text = { "Invert HTL Literal quotes" }
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
