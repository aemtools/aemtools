package com.aemtools.completion.small.reppolicy

import com.aemtools.common.util.closest
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.lookupElement
import com.aemtools.completion.small.inserthandler.JcrNameArrayInsertHandler
import com.aemtools.completion.small.patterns.RepPolicyPatterns
import com.aemtools.completion.small.patterns.RepPolicyPatterns.attributeNameUnderAcl
import com.aemtools.completion.small.patterns.RepPolicyPatterns.primaryTypeInAcl
import com.aemtools.completion.small.patterns.RepPolicyPatterns.privilegesValue
import com.aemtools.completion.small.patterns.RepPolicyPatterns.repRestrictionAttributeName
import com.aemtools.completion.small.patterns.RepPolicyPatterns.tagUnderAclRoot
import com.aemtools.completion.small.reppolicy.provider.FunctionalCompletionProvider
import com.aemtools.lang.jcrproperty.psi.JpArray
import com.aemtools.lang.jcrproperty.psi.JpArrayValue
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.psi.xml.XmlTag

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

  extend(
      CompletionType.BASIC,
      repRestrictionAttributeName,
      FunctionalCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("jcr:primaryType")
                .withInsertHandler(XmlAttributeInsertHandler()),
            lookupElement("rep:glob")
                .withInsertHandler(XmlAttributeInsertHandler())
        )
      }, shouldStop = true))

  extend(CompletionType.BASIC,
      RepPolicyPatterns.tagUnderAllowOrDeny,
      FunctionalCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("rep:restrictions")
        )
      }, shouldStop = true))

  extend(
      CompletionType.BASIC,
      tagUnderAclRoot,
      FunctionalCompletionProvider({ parameters, _, _ ->
        val originalPosition = parameters.originalPosition
        val text = originalPosition?.text

        val parentTag = parameters.position.findParentByType(
            XmlTag::class.java,
            { it.name == "jcr:root" },
            true)

        val childTags = parentTag?.findChildrenByType(XmlTag::class.java)
            ?.map {
              it.name
            } ?: emptyList()

        when {
          text == null -> listOf(
              lookupElement("allow"),
              lookupElement("deny")
          )
          else -> {
            var closest = text.closest(setOf("allow", "deny"))
                ?: "allow"

            while (childTags.contains(closest)) {
              val first = if (closest.contains("allow")) {
                "allow"
              } else {
                "deny"
              }

              val second = closest.substringAfter(first)

              val number = second.toIntOrNull()
                  ?: -1

              closest = "$first${number + 1}"
            }

            listOf(lookupElement(closest))
          }
        }

      }, shouldStop = true)
  )

  extend(CompletionType.BASIC,
      attributeNameUnderAcl,
      FunctionalCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("jcr:primaryType")
                .withInsertHandler(XmlAttributeInsertHandler()),
            lookupElement("rep:principalName")
                .withInsertHandler(JcrNameArrayInsertHandler()),
            lookupElement("rep:privileges")
                .withInsertHandler(XmlAttributeInsertHandler())
        )
      })
  )

  extend(CompletionType.BASIC,
      privilegesValue,
      FunctionalCompletionProvider({ parameters, _, _ ->
        val currentPosition = parameters.position

        val array = currentPosition.findParentByType(JpArray::class.java)

        val values = array?.findChildrenByType(JpArrayValue::class.java)
            ?.map { it.text }

        val result = listOf(
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

        if (values != null) {
          result.filterNot { values.contains(it.lookupString) }
        } else {
          result
        }
      })
  )

}
}
