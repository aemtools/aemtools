package com.aemtools.lang.el

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue

/**
 * @author Dmytro Troynikov
 */
class ElInjector : MultiHostInjector {
  override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>>
      = listOf(XmlAttributeValue::class.java).toMutableList()

  override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {

  }

}
