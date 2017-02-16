package com.aemtools.completion.htl

import com.aemtools.completion.htl.provider.*
import com.aemtools.lang.htl.psi.pattern.HtlPatterns
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

/**
 * @author Dmytro Troynikov.
 */
class HtlElCompletionContributor : CompletionContributor() {init {
    extend(CompletionType.BASIC, HtlPatterns.memberAccess, HtlElMemberAccessCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.variableName, HtlElVariableNameCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.stringLiteralValue, HtlElStringLiteralCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.optionName, HtlElOptionCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.contextOptionAssignment, HtlElContextOptionAssignmentCompletionProvider)
}
}