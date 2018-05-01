package com.aemtools.completion.small.reppolicy

import com.aemtools.common.util.lookupElement
import com.aemtools.completion.small.patterns.RepPolicyPatterns.attributeNameUnderAcl
import com.aemtools.completion.small.patterns.RepPolicyPatterns.primaryTypeInAcl
import com.aemtools.completion.small.patterns.RepPolicyPatterns.privilegesValue
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

  extend(CompletionType.BASIC,
      attributeNameUnderAcl,
      FunctionalCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("jcr:primaryType"),
            lookupElement("rep:principalName"),
            lookupElement("rep:privileges")
        )
      })
  )

  extend(CompletionType.BASIC,
      privilegesValue,
      FunctionalCompletionProvider({ _, _, _ ->
        listOf(
            "jcr:versionManagement",
            "jcr:addChildNodes",
            "jcr:readAccessControl",
            "jcr:nodeTypeDefinitionManagement",
            "jcr:lifecycleManagement",
            "jcr:retentionManagement",
            "rep:alterProperties",
            "jcr:removeNode",
            "rep:readProperties",
            "rep:indexDefinitionManagement",
            "jcr:write",
            "rep:privilegeManagement",
            "jcr:workspaceManagement",
            "rep:removeProperties",
            "jcr:modifyAccessControl",
            "jcr:namespaceManagement",
            "rep:userManagement",
            "jcr:read",
            "rep:write",
            "rep:addProperties",
            "jcr:lockManagement",
            "jcr:nodeTypeManagement",
            "jcr:removeChildNodes",
            "jcr:modifyProperties",
            "rep:readNodes",
            "crx:replicate"
        ).map { lookupElement(it) }
      })
  )

}
}
