package com.aemtools.completion.small.clientlibraryfolder

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.completion.small.clientlibraryfolder.provider.JcrTypeCompletionProvider
import com.aemtools.completion.small.clientlibraryfolder.provider.ValueOfCategoriesCompletionProvider
import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfCategories
import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfDependencies
import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfEmbeds
import com.aemtools.completion.small.patterns.JcrPatterns.jcrType

/**
 * @author Dmytro Primshyts
 */
class JcrClientLibraryFolderCompletionContributor : BaseCompletionContributor({
  basic(
      jcrArrayValueOfCategories,
      ValueOfCategoriesCompletionProvider
  )

  basic(
      jcrArrayValueOfDependencies,
      ValueOfCategoriesCompletionProvider
  )

  basic(
      jcrArrayValueOfEmbeds,
      ValueOfCategoriesCompletionProvider
  )

  basic(
      jcrType,
      JcrTypeCompletionProvider
  )
})


