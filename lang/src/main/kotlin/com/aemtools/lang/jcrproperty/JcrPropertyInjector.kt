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
  override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>>
      = mutableListOf(XmlAttributeValue::class.java)

  override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
    val attributeValue = context as? XmlAttributeValue ?: return

    val attributeName = attributeValue.findParentByType(XmlAttribute::class.java)
        ?: return

    if (context.containingFile.name != ".content.xml") {
      return
    }

    if (context is PsiLanguageInjectionHost
        && attributeName.name in listOf("embed", "categories", "channels")) {
      registrar.startInjecting(JcrPropertyLanguage)
      registrar.addPlace(
          "prefix", "suffix",
          context,
          TextRange.create(1, attributeValue.text.length - 1)
      )
    }
  }

}
