package com.aemtools.completion.small.editconfig

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.common.completion.BaseCompletionProvider
import com.aemtools.common.completion.lookupElement
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.completion.small.inserthandler.JcrArrayInsertHandler
import com.aemtools.completion.small.patterns.EditConfigPatterns.attributeUnderJcrRoot
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqActionsValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqDialogModeValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqInheritValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqLayoutValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.primaryTypeInEditConfig
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro Primshyts
 */
class EditConfigCompletionContributor : BaseCompletionContributor({
  basic(
      cqLayoutValue,

      BaseCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("rollover"),
            lookupElement("editbar"),
            lookupElement("auto")
        )
      })
  )

  basic(
      cqInheritValue,

      BaseCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("true"),
            lookupElement("false")
        )
      })
  )

  basic(
      cqDialogModeValue,

      BaseCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("floating"),
            lookupElement("inline"),
            lookupElement("auto")
        )
      })
  )

  basic(
      attributeUnderJcrRoot,

      BaseCompletionProvider({ parameters, _, _ ->
        val presentNames = parameters.position.findParentByType(XmlTag::class.java)
            ?.findChildrenByType(XmlAttribute::class.java)
            ?.map { it.name } ?: emptyList()

        val result = listOf(
            lookupElement("jcr:primaryType")
                .withInsertHandler(XmlAttributeInsertHandler()),
            lookupElement("cq:actions")
                .withInsertHandler(JcrArrayInsertHandler()),
            lookupElement("cq:layout")
                .withInsertHandler(XmlAttributeInsertHandler()),
            lookupElement("cq:dialogMode")
                .withInsertHandler(XmlAttributeInsertHandler()),
            lookupElement("cq:emptyText")
                .withInsertHandler(XmlAttributeInsertHandler()),
            lookupElement("cq:inherit")
                .withInsertHandler(XmlAttributeInsertHandler())
        )
        result.filterNot { presentNames.contains(it.lookupString) }
      })
  )

  basic(
      primaryTypeInEditConfig,
      BaseCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("cq:EditConfig")
        )
      })
  )

  basic(
      cqActionsValue,
      BaseCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("text:"),
            lookupElement("-"),
            lookupElement("edit"),
            lookupElement("delete"),
            lookupElement("insert"),
            lookupElement("copymove"),
            lookupElement("_clear")
        )
      })
  )
})
