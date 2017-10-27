package com.aemtools.completion.htl

import com.aemtools.completion.htl.provider.HtlDataSlyUseCompletionProvider
import com.aemtools.completion.htl.provider.HtlElDataSlyCallVariableCompletionProvider
import com.aemtools.completion.htl.provider.HtlElMemberAccessCompletionProvider
import com.aemtools.completion.htl.provider.HtlElVariableNameCompletionProvider
import com.aemtools.completion.htl.provider.HtlI18NKeyCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlContextOptionAssignmentCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlDataSlyCallOptionCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlDataSlyResourceOptionCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlDataSlyTemplateOptionCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlOptionCompletionProvider
import com.aemtools.completion.htl.provider.option.HtlResourceTypeOptionAssignmentCompletionProvider
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.contextOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyCallOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyResourceOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyTemplateOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseMainString
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.mainVariableInsideOfDataSlyCall
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.memberAccess
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.optionName
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.resourceTypeOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.stringLiteralValue
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.variableName
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType.BASIC
import com.intellij.codeInsight.completion.CompletionType.SMART

/**
 * @author Dmytro Troynikov.
 */
class HtlElCompletionContributor : CompletionContributor() { init {
  extend(BASIC, memberAccess, HtlElMemberAccessCompletionProvider)

  extend(BASIC, mainVariableInsideOfDataSlyCall, HtlElDataSlyCallVariableCompletionProvider)

  extend(BASIC, dataSlyUseMainString, HtlDataSlyUseCompletionProvider)
  extend(SMART, dataSlyUseMainString, HtlDataSlyUseCompletionProvider)

  extend(BASIC, stringLiteralValue, HtlI18NKeyCompletionProvider)

  extend(BASIC, variableName, HtlElVariableNameCompletionProvider)

  extend(BASIC, dataSlyTemplateOption, HtlDataSlyTemplateOptionCompletionProvider)
  extend(BASIC, dataSlyCallOption, HtlDataSlyCallOptionCompletionProvider)
  extend(BASIC, dataSlyResourceOption, HtlDataSlyResourceOptionCompletionProvider)
  extend(BASIC, optionName, HtlOptionCompletionProvider)

  extend(BASIC, contextOptionAssignment, HtlContextOptionAssignmentCompletionProvider)
  extend(BASIC, resourceTypeOptionAssignment, HtlResourceTypeOptionAssignmentCompletionProvider)
}
}
