package com.aemtools.codeinsight.xml.schema

import com.aemtools.lang.util.isFileWithCqNamespace
import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

class CqNamespaceImplicitUsageProvider : ImplicitUsageProvider {
  override fun isImplicitUsage(element: PsiElement): Boolean {
    if (element is XmlAttribute
        && element.isNamespaceDeclaration
        && element.parent.isFileWithCqNamespace()) {

      return element.localName == "cq"
    }
    return false
  }

  override fun isImplicitRead(element: PsiElement): Boolean = false

  override fun isImplicitWrite(element: PsiElement): Boolean = false
}
