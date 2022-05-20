package com.aemtools.documentation.clientlibs

import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfCategories
import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfDependencies
import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfEmbeds
import com.aemtools.documentation.clientlibs.generator.ClientlibDocumentationGenerator
import com.aemtools.lang.jcrproperty.psi.JpArrayValue
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * Provides category documentation for category from categories, embeds and dependencies properties.
 *
 * @author Kostiantyn Diachenko
 */
open class ClientlibsFolderDocumentationProvider : AbstractDocumentationProvider() {

  override fun getCustomDocumentationElement(editor: Editor,
                                             file: PsiFile,
                                             contextElement: PsiElement?,
                                             targetOffset: Int): PsiElement? {
    return contextElement?.parent as? JpArrayValue
  }

  override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
    if (originalElement == null) {
      return super.generateDoc(element, originalElement)
    }

    if (jcrArrayValueOfCategories.accepts(originalElement)
        || jcrArrayValueOfDependencies.accepts(originalElement)
        || jcrArrayValueOfEmbeds.accepts(originalElement)) {

      val categoryPsiElement = element as? JpArrayValue
      val category = categoryPsiElement?.arrayValueToken?.text
          ?: return super.generateDoc(element, originalElement)

      return ClientlibDocumentationGenerator.generateDoc(categoryPsiElement, category)
          ?: super.generateDoc(element, originalElement)

    }
    return super.generateDoc(element, originalElement)
  }
}
