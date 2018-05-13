package com.aemtools.lang.jcrproperty

import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.hasAttribute
import com.aemtools.common.util.hasParent
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro Primshyts
 */
class JcrPropertyInjector : MultiHostInjector {
  override fun elementsToInjectIn()
      : MutableList<out Class<out PsiElement>> = mutableListOf(XmlAttributeValue::class.java)

  override fun getLanguagesToInject(
      registrar: MultiHostRegistrar,
      context: PsiElement) {
    val attributeValue = context as? XmlAttributeValue ?: return

    val attributeName = attributeValue.findParentByType(XmlAttribute::class.java)
        ?: return

    val psiLanguageInjectionHost = context
        as? PsiLanguageInjectionHost
        ?: return

    when {
    // inject into cq:ClientLibraryFolder
      psiLanguageInjectionHost.containingFile.name == ".content.xml"
          && psiLanguageInjectionHost.hasParent(cqClientLibraryFolderTag())
          && attributeName.name in listOf(
          "jcr:primaryType",
          "embed",
          "categories",
          "channels",
          "dependencies"
      ) -> inject(registrar, context, attributeValue)

    // inject into _rep_policy.xml
      psiLanguageInjectionHost.containingFile.name == "_rep_policy.xml"
          && attributeName.name in listOf(
          "jcr:primaryType",
          "rep:principalName",
          "rep:privileges"
      ) -> inject(registrar, context, attributeValue)

      psiLanguageInjectionHost.containingFile.name == "_cq_editConfig.xml"
          && attributeName.name in listOf(
          "jcr:primaryType",
          "cq:actions",
          "cq:layout",
          "cq:dialogMode",
          "cq:emptyText",
          "cq:inherit"
      ) -> inject(registrar, context, attributeValue)
    }

  }

  private fun cqClientLibraryFolderTag(): (PsiElement) -> Boolean {
    return { parent ->
      parent is XmlTag
          && parent.hasAttribute { attribute ->
        attribute.name == "jcr:primaryType"
            && attribute.value == "cq:ClientLibraryFolder"
      }
    }
  }

  private fun inject(
      registrar: MultiHostRegistrar,
      context: PsiLanguageInjectionHost,
      attributeValue: XmlAttributeValue) {
    registrar.startInjecting(JcrPropertyLanguage)
    registrar.addPlace(
        null, null,
        context,
        TextRange.create(1, attributeValue.text.length - 1)
    )
    registrar.doneInjecting()
  }

}
