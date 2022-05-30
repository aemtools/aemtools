package com.aemtools.completion.common.provider

import com.aemtools.common.completion.BaseCompletionProvider
import com.aemtools.common.completion.lookupElement

/**
 * Basic boolean value completion provider.
 *
 * @author Kostiantyn Diachenko
 */
object JpBooleanValueCompletionProvider : BaseCompletionProvider({ _, _, _ ->
  listOf(
      lookupElement("true"),
      lookupElement("false")
  )
})
