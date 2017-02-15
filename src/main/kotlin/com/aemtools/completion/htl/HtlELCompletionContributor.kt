package com.aemtools.completion.htl

import com.aemtools.completion.htl.provider.HtlElContextOptionAssignmentCompletionProvider
import com.aemtools.completion.htl.provider.HtlElOptionCompletionProvider
import com.aemtools.completion.htl.provider.HtlElStringLiteralCompletionProvider
import com.aemtools.completion.htl.provider.HtlElVariableNameCompletionProvider
import com.aemtools.lang.htl.psi.pattern.HtlPatterns
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns

/**
 * @author Dmytro Troynikov.
 */
class HtlELCompletionContributor : CompletionContributor() {init {
    extend(CompletionType.BASIC, PlatformPatterns.psiElement(), HtlELCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.variableName, HtlElVariableNameCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.stringLiteralValue, HtlElStringLiteralCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.optionName, HtlElOptionCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.contextOptionAssignment, HtlElContextOptionAssignmentCompletionProvider)
}
}