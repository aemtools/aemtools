package com.aemtools.analysis.htl.callchain.elements.segment

import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor

/**
 * Class that represents empty call chain.
 *
 * @author Dmytro Troynikov
 */
class EmptyCallChainSegment : CallChainSegment {
  override fun outputType(): TypeDescriptor = TypeDescriptor.empty()

  override fun inputType(): TypeDescriptor = TypeDescriptor.empty()

  override fun chainElements(): List<CallChainElement> = listOf()
}
