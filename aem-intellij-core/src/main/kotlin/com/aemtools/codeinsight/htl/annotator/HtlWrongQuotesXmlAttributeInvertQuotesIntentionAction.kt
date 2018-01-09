package com.aemtools.codeinsight.htl.annotator

import com.aemtools.common.intention.BaseHtlIntentionAction
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.isDoubleQuoted
import com.aemtools.common.util.psiDocumentManager
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.lang.util.getHtlFile
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.templateLanguages.OuterLanguageElement
import com.intellij.psi.xml.XmlAttribute

/**
 * Inverts [XmlAttribute] quotes.
 *
 * @author Dmytro Troynikov
 */
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
