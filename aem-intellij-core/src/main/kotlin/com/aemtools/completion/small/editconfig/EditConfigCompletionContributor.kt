package com.aemtools.completion.small.editconfig

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.common.completion.BaseCompletionProvider
import com.aemtools.common.completion.lookupElement
import com.aemtools.completion.small.inserthandler.JcrArrayInsertHandler
import com.aemtools.completion.small.patterns.EditConfigPatterns.attributeUnderJcrRoot
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqActionsValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqDialogModeValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqInheritValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqLayoutValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.primaryTypeInEditConfig

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

      BaseCompletionProvider({ _, _, _ ->
        listOf(
            lookupElement("jcr:primaryType"),
            lookupElement("cq:actions")
                .withInsertHandler(JcrArrayInsertHandler()),
            lookupElement("cq:layout"),
            lookupElement("cq:dialogMode"),
            lookupElement("cq:emptyText"),
            lookupElement("cq:inherit")
        )
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
