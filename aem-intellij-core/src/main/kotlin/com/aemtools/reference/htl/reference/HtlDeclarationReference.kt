package com.aemtools.reference.htl.reference

import com.aemtools.analysis.htl.callchain.elements.BaseChainElement
import com.aemtools.reference.htl.HtlDeclarationIdentifier
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.xml.XmlAttribute

/**
 * Reference to htl variable declaration (like `data-sly-use` or `data-sly-test` spawned variables).
 *
 * @author Dmytro Primshyts
 */
class HtlDeclarationReference(
    val xmlAttribute: XmlAttribute?,
    val callChainElement: BaseChainElement?,
    holder: PsiElement,
    range: TextRange)
  : PsiReferenceBase<PsiElement>(holder, range, true) {

  override fun resolve(): PsiElement? {
    val psiClass = callChainElement?.type?.asResolutionResult()?.psiClass
    return when {
      xmlAttribute != null -> {
        HtlDeclarationIdentifier(xmlAttribute)
      }
      psiClass != null -> {
        psiClass
      }
      else -> {
        null
      }
    }
  }

  override fun isReferenceTo(element: PsiElement): Boolean {
    return super.isReferenceTo(element) || xmlAttribute?.isEquivalentTo(element) ?: false
  }

  override fun getVariants(): Array<Any> = emptyArray()

}
