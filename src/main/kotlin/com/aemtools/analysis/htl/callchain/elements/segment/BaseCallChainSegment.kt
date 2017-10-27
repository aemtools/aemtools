package com.aemtools.analysis.htl.callchain.elements.segment

import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration

/**
 * Base implementation of [CallChainSegment].
 *
 * @author Dmytro Troynikov
 */
class BaseCallChainSegment(
    private val input: TypeDescriptor,
    private val output: TypeDescriptor,
    val declaration: HtlVariableDeclaration?,
    private val elements: List<CallChainElement>) : CallChainSegment {
  override fun inputType(): TypeDescriptor = input

  override fun outputType(): TypeDescriptor = output

  override fun chainElements(): List<CallChainElement> = elements
}
