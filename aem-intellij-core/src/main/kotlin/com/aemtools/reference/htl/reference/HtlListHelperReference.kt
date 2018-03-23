package com.aemtools.reference.htl.reference

import com.aemtools.reference.htl.HtlListHelperDeclarationIdentifier
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlListHelperReference(val xmlAttribute: XmlAttribute,
                             holder: PsiElement,
                             range: TextRange) :
    PsiReferenceBase<PsiElement>(holder, range, true) {
  override fun resolve(): PsiElement? = HtlListHelperDeclarationIdentifier(xmlAttribute)

  override fun getVariants(): Array<Any> {
    return arrayOf()
  }
}
