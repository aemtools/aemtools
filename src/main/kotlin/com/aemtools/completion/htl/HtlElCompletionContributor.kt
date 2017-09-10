package com.aemtools.completion.htl

import com.aemtools.completion.htl.provider.*
import com.aemtools.completion.htl.provider.option.*
import com.aemtools.lang.htl.psi.pattern.HtlPatterns
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

/**
 * @author Dmytro Troynikov.
 */
class HtlElCompletionContributor : CompletionContributor() { init {
    extend(CompletionType.BASIC, HtlPatterns.memberAccess, HtlElMemberAccessCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.mainVariableInsideOfDataSlyCall, HtlElDataSlyCallVariableCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.dataSlyUseMainString, HtlDataSlyUseCompletionProvider)
    extend(CompletionType.SMART, HtlPatterns.dataSlyUseMainString, HtlDataSlyUseCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.stringLiteralValue, HtlI18NKeyCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.variableName, HtlElVariableNameCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.dataSlyTemplateOption, HtlDataSlyTemplateOptionCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.dataSlyCallOption, HtlDataSlyCallOptionCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.dataSlyResourceOption, HtlDataSlyResourceOptionCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.optionName, HtlOptionCompletionProvider)

    extend(CompletionType.BASIC, HtlPatterns.contextOptionAssignment, HtlContextOptionAssignmentCompletionProvider)
    extend(CompletionType.BASIC, HtlPatterns.resourceTypeOptionAssignment, HtlResourceTypeOptionAssignmentCompletionProvider)
}
}