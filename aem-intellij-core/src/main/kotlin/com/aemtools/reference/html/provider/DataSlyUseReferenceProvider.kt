package com.aemtools.reference.html.provider

import com.aemtools.common.util.findParentByType
import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.util.isDataSlyUse
import com.aemtools.reference.common.reference.PsiFileReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Primshyts
 */
object DataSlyUseReferenceProvider : PsiReferenceProvider() {

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
    val attr = element.findParentByType(XmlAttribute::class.java) ?: return emptyArray()
    val valueElement = attr.valueElement ?: return emptyArray()
    val value = valueElement.value
    if (attr.isDataSlyUse()) {
      val psiFile = HtlIndexFacade.resolveUseFile(value, attr.containingFile)

      if (psiFile != null) {
        val fileReference = PsiFileReference(psiFile,
            valueElement,
            TextRange(1, valueElement.textLength - 1))
        return arrayOf(fileReference)
      }
    }
    return emptyArray()
  }

}
