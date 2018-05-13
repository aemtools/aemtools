package com.aemtools.completion.small.clientlibraryfolder

import com.aemtools.completion.small.clientlibraryfolder.provider.ClientLibraryFolderCompletionProvider
import com.aemtools.completion.small.patterns.JcrPatterns
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType.BASIC

/**
 * @author Dmytro Primshyts
 */
class ClientLibraryFolderCompletionContributor : CompletionContributor() {init {

  extend(BASIC, JcrPatterns.attributeInClientLibraryFolder,
      ClientLibraryFolderCompletionProvider)

}
}
