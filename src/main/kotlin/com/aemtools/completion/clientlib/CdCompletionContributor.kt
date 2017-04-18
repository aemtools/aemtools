package com.aemtools.completion.clientlib

import com.aemtools.completion.clientlib.provider.ClientlibDeclarationBasePathCompletionProvider
import com.aemtools.lang.clientlib.psi.pattern.CdPatterns
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov
 */
class CdCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, CdPatterns.basePath, ClientlibDeclarationBasePathCompletionProvider)

        extend(CompletionType.BASIC, psiElement(), CdCompletionProvider())
    }
}

class CdCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
        result.addElement(LookupElementBuilder.create("hello"))
    }

}