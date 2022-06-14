package com.aemtools.completion.small.component

import com.aemtools.common.completion.BaseCompletionContributor
import com.aemtools.completion.small.component.provider.CqComponentCompletionProvider
import com.aemtools.completion.small.patterns.CqComponentPatterns.attributeInCqComponent

/**
 * Completion contributor for attributes of the node with jcr:primaryType="cq:Component".
 *
 * @author Kostiantyn Diachenko
 */
class CqComponentCompletionContributor : BaseCompletionContributor({
  basic(attributeInCqComponent, CqComponentCompletionProvider)
})
