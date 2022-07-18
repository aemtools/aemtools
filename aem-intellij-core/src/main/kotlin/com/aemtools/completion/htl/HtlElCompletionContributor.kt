package com.aemtools.completion.htl

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.common.constant.const.htl.DATA_SLY_LIST
import com.aemtools.common.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.completion.htl.provider.*
import com.aemtools.completion.htl.provider.option.*
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.categoriesOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.categoriesOptionAssignmentViaArray
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.contextOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyCallOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyListOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyRepeatOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyResourceOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyTemplateOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseMainString
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.mainVariableInsideOfDataSlyCall
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.mainVariableInsideOfDataSlyList
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.mainVariableInsideOfDataSlyRepeat
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.memberAccess
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.optionName
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.resourceTypeOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.stringLiteralValue
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.variableName
import com.intellij.patterns.StandardPatterns.or

/**
 * @author Dmytro Primshyts.
 */
class HtlElCompletionContributor : BaseCompletionContributor({
  basic(memberAccess, HtlElMemberAccessCompletionProvider)

  basic(mainVariableInsideOfDataSlyCall, HtlElDataSlyCallVariableCompletionProvider)

  basic(dataSlyUseMainString, HtlDataSlyUseCompletionProvider)
  smart(dataSlyUseMainString, HtlDataSlyUseCompletionProvider)

  basic(stringLiteralValue, HtlI18NKeyCompletionProvider)

  basic(variableName, HtlElVariableNameCompletionProvider)

  basic(dataSlyTemplateOption, HtlDataSlyTemplateOptionCompletionProvider)
  basic(dataSlyCallOption, HtlDataSlyCallOptionCompletionProvider)
  basic(dataSlyResourceOption, HtlDataSlyResourceOptionCompletionProvider)

  basic(dataSlyListOption, HtlDataSlyIterableOptionCompletionProvider(DATA_SLY_LIST))
  basic(dataSlyRepeatOption, HtlDataSlyIterableOptionCompletionProvider(DATA_SLY_REPEAT))

  basic(optionName, HtlOptionCompletionProvider)

  smart(or(mainVariableInsideOfDataSlyList, mainVariableInsideOfDataSlyRepeat),
      HtlListSmartCompletionProvider)

  basic(contextOptionAssignment, HtlContextOptionAssignmentCompletionProvider)
  basic(resourceTypeOptionAssignment, HtlResourceTypeOptionAssignmentCompletionProvider)

  basic(categoriesOptionAssignment, HtlClientLibraryTemplateCategoryCompletionProvider)
  basic(categoriesOptionAssignmentViaArray, HtlClientLibraryTemplateCategoryCompletionProvider)

})
