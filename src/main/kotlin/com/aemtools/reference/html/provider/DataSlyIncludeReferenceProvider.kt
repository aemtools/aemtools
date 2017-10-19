package com.aemtools.reference.html.provider

import com.aemtools.reference.common.provider.DataSlyIncludeReferenceProviderBase
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object DataSlyIncludeReferenceProvider : DataSlyIncludeReferenceProviderBase() {

  override fun name(element: PsiElement, context: ProcessingContext): String? {
    val valueElement = element as? XmlAttributeValue
    return valueElement?.value
  }

}
