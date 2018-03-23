package com.aemtools.completion.clientlib

import com.aemtools.completion.clientlib.provider.ClientlibDeclarationBasePathCompletionProvider
import com.aemtools.completion.clientlib.provider.ClientlibDeclarationIncludeCompletionProvider
import com.aemtools.lang.clientlib.psi.pattern.CdPatterns
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

/**
 * @author Dmytro_Troynikov
 */
class CdCompletionContributor : CompletionContributor() {
  init {
    extend(CompletionType.BASIC, CdPatterns.basePath, ClientlibDeclarationBasePathCompletionProvider)

    extend(CompletionType.BASIC, CdPatterns.include, ClientlibDeclarationIncludeCompletionProvider)
  }
}
