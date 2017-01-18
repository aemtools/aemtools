package com.aemtools.completion.htl

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns

/**
 * @author Dmytro Troynikov.
 */
class HtlELCompletionContributor : CompletionContributor {
    constructor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), HtlELCompletionProvider)
    }
}