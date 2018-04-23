package com.aemtools.lang.jcrproperty

import com.aemtools.common.util.findParentByType
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue

/**
 * @author Dmytro Primshyts
 */
class JcrPropertyInjector : MultiHostInjector {
  override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> = mutableListOf(XmlAttributeValue::class.java)

  override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
    val attributeValue = context as? XmlAttributeValue ?: return

    val attributeName = attributeValue.findParentByType(XmlAttribute::class.java)
        ?: return

    val psiLanguageInjectionHost = context
        as? PsiLanguageInjectionHost
        ?: return

    if (psiLanguageInjectionHost.containingFile.name == ".content.xml") {
      if (attributeName.name in listOf(
              "embed",
              "categories",
              "channels",
              "dependencies"
          )) {
        inject(registrar, context, attributeValue)
      }
      return
    }

    if (psiLanguageInjectionHost.containingFile.name == "_rep_policy.xml") {
      if (attributeName.name in listOf(
              "jcr:primaryType"
          )) {
        inject(registrar, context, attributeValue)
      }
    }
  }

  private fun inject(registrar: MultiHostRegistrar, context: PsiLanguageInjectionHost, attributeValue: XmlAttributeValue) {
    registrar.startInjecting(JcrPropertyLanguage)
    registrar.addPlace(
        null, null,
        context,
        TextRange.create(1, attributeValue.text.length - 1)
    )
    registrar.doneInjecting()
  }

}
