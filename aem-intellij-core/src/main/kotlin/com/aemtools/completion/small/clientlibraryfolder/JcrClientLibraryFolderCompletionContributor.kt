package com.aemtools.completion.small.clientlibraryfolder

import com.aemtools.completion.small.clientlibraryfolder.provider.JcrTypeCompletionProvider
import com.aemtools.completion.small.clientlibraryfolder.provider.ValueOfCategoriesCompletionProvider
import com.aemtools.completion.small.patterns.JcrPatterns.jcrArrayValueOfCategories
import com.aemtools.completion.small.patterns.JcrPatterns.jcrType
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

/**
 * @author Dmytro Primshyts
 */
class JcrClientLibraryFolderCompletionContributor : CompletionContributor() {init {
  extend(
      CompletionType.BASIC,
      jcrArrayValueOfCategories,
      ValueOfCategoriesCompletionProvider
  )

  extend(
      CompletionType.BASIC,
      jcrType,
      JcrTypeCompletionProvider
  )
}
}
