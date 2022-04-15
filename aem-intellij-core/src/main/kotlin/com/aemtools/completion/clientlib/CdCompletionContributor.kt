package com.aemtools.completion.clientlib

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.completion.clientlib.provider.ClientlibDeclarationBasePathCompletionProvider
import com.aemtools.completion.clientlib.provider.ClientlibDeclarationIncludeCompletionProvider
import com.aemtools.lang.clientlib.psi.pattern.CdPatterns

/**
 * @author Dmytro Primshyts
 */
class CdCompletionContributor : BaseCompletionContributor({

  basic(CdPatterns.basePath, ClientlibDeclarationBasePathCompletionProvider)
  basic(CdPatterns.include, ClientlibDeclarationIncludeCompletionProvider)

})
