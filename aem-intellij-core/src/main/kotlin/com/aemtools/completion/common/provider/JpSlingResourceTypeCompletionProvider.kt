package com.aemtools.completion.common.provider

import com.aemtools.common.completion.BaseCompletionProvider
import com.aemtools.completion.htl.common.SlingResourceTypesCompletionResolver

/**
 * Sling resource types completion provider in the Jcr Property value.
 *
 * @author Kostiantyn Diachenko
 */
object JpSlingResourceTypeCompletionProvider : BaseCompletionProvider({ parameters, _, _ ->
  SlingResourceTypesCompletionResolver.resolveDeclarations(parameters.position.project)
})
