package com.aemtools.reference.htl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlListHelperDeclarationIdentifier(
    val attribute: XmlAttribute
) : HtlDeclarationIdentifier(attribute), PsiNamedElement {
  override fun getParent(): PsiElement = attribute
  override fun getName(): String? {
    return super.getName() + "List"
  }

  override fun getText(): String? {
    return super.getText() + "List"
  }
}
