package com.aemtools.inspection.xml.fix

import com.intellij.codeInsight.daemon.impl.analysis.CreateNSDeclarationIntentionFix
import com.intellij.psi.PsiElement

class AddMissedNamespaceAction(element: PsiElement, private val namespacePrefix: String)
  : CreateNSDeclarationIntentionFix(element, namespacePrefix) {
  override fun getText(): String {
    return "Add missed '${namespacePrefix}' namespace"
  }
}
