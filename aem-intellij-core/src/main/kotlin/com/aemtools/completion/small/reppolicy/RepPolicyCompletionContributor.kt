package com.aemtools.completion.small.reppolicy

import com.aemtools.common.util.lookupElement
import com.aemtools.completion.small.patterns.RepPolicyPatterns.primaryTypeInAcl
import com.aemtools.completion.small.reppolicy.provider.FunctionalCompletionProvider
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType

/**
 * @author Dmytro Primshyts.
 */
class RepPolicyCompletionContributor : CompletionContributor() {init {

  extend(CompletionType.BASIC,
      primaryTypeInAcl,
      FunctionalCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("rep:GrantACE"),
            lookupElement("rep:DenyACE")
        )
      })
  )

}
}
