package com.aemtools.analysis.htl.callchain.elements.segment

import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor

/**
 * @Author Dmytro_Troynikov
 */
interface CallChainSegment {

  /**
   * [TypeDescriptor] for output type.
   *
   * @return output type descriptor
   */
  fun outputType(): TypeDescriptor

  /**
   * [TypeDescriptor] for input type.
   *
   * @return input type descriptor
   */
  fun inputType(): TypeDescriptor

  /**
   * List of [CallChainElement] items.
   *
   * @return list of call chain elements
   */
  fun chainElements(): List<CallChainElement>

  companion object {
    private val EMPTY = EmptyCallChainSegment()

    /**
     * Create empty call chain segment.
     *
     * @return empty call chain segment
     */
    fun empty(): CallChainSegment = EMPTY
  }
}
