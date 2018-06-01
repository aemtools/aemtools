package com.aemtools.completion.small.clientlibraryfolder

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.completion.small.clientlibraryfolder.provider.ClientLibraryFolderCompletionProvider
import com.aemtools.completion.small.patterns.JcrPatterns.attributeInClientLibraryFolder

/**
 * @author Dmytro Primshyts
 */
class ClientLibraryFolderCompletionContributor : BaseCompletionContributor({
  basic(attributeInClientLibraryFolder, ClientLibraryFolderCompletionProvider)
})
