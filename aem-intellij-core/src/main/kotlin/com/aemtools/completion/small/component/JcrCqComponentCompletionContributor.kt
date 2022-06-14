package com.aemtools.completion.small.component

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.completion.common.provider.JpBooleanValueCompletionProvider
import com.aemtools.completion.common.provider.JpSlingResourceTypeCompletionProvider
import com.aemtools.completion.small.component.provider.CqComponentGroupCompletionProvider
import com.aemtools.completion.small.patterns.CqComponentPatterns.cqComponentComponentGroup
import com.aemtools.completion.small.patterns.CqComponentPatterns.cqComponentIsContainer
import com.aemtools.completion.small.patterns.CqComponentPatterns.cqComponentNoDecoration
import com.aemtools.completion.small.patterns.CqComponentPatterns.cqComponentSlingResourceSuperType

/**
 * Completion provider for attributes values in the node with jcr:primaryType="cq:Component".
 *
 * @author Kostiantyn Diachenko
 */
class JcrCqComponentCompletionContributor : BaseCompletionContributor({
  basic(cqComponentComponentGroup, CqComponentGroupCompletionProvider)
  basic(cqComponentSlingResourceSuperType, JpSlingResourceTypeCompletionProvider)
  basic(cqComponentIsContainer, JpBooleanValueCompletionProvider)
  basic(cqComponentNoDecoration, JpBooleanValueCompletionProvider)
})
