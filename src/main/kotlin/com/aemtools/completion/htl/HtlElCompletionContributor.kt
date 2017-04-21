package com.aemtools.completion.htl

import com.aemtools.completion.htl.provider.*
import com.aemtools.completion.htl.provider.option.HtlDataSlyCallOptionCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlDataSlyTemplateOptionCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlElContextOptionAssignmentCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlElOptionCompletionProvider
import com.aemtools.lang.htl.psi.pattern.HtlPatterns
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

/**
 * @author Dmytro Troynikov.
 */
class HtlElCompletionContributor : CompletionContributor() {init {
    extend(CompletionType.BASIC, HtlPatterns.memberAccess, HtlElMemberAccessCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.mainVariableInsideOfDataSlyCall, HtlElDataSlyCallVariableCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.dataSlyUseMainString, HtlDataSlyUseCompletionProvider)
    extend(CompletionType.SMART, HtlPatterns.dataSlyUseMainString, HtlDataSlyUseCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.variableName, HtlElVariableNameCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.dataSlyTemplateOption, HtlDataSlyTemplateOptionCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.dataSlyCallOption, HtlDataSlyCallOptionCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.optionName, HtlElOptionCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.contextOptionAssignment, HtlElContextOptionAssignmentCompletionProvider)
}
}