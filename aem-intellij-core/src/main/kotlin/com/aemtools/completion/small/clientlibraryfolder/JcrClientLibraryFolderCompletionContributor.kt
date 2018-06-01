package com.aemtools.completion.small.clientlibraryfolder

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.completion.small.clientlibraryfolder.provider.JcrTypeCompletionProvider
import com.aemtools.completion.small.clientlibraryfolder.provider.ValueOfCategoriesCompletionProvider
import com.aemtools.completion.small.patterns.JcrPatterns.jcrArrayValueOfCategories
import com.aemtools.completion.small.patterns.JcrPatterns.jcrType
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

/**
 * @author Dmytro Primshyts
 */
class JcrClientLibraryFolderCompletionContributor : BaseCompletionContributor({
  basic(
      jcrArrayValueOfCategories,
      ValueOfCategoriesCompletionProvider
  )

  basic(
      jcrType,
      JcrTypeCompletionProvider
  )
})


