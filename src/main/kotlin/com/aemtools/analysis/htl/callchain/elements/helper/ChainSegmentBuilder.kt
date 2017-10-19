package com.aemtools.analysis.htl.callchain.elements.helper

import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.elements.segment.BaseCallChainSegment
import com.aemtools.analysis.htl.callchain.elements.segment.CallChainSegment
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration

/**
 * Segment data holder.
 *
 * @property inputType
 *              input type descriptor.
 * @property outputType
 *              output type descriptor.
 * @property declarationType
 *              declaration type.
 * @property chain
 *              call chain element list.
 */
class SegmentDataHolder(var inputType: TypeDescriptor = TypeDescriptor.empty(),
                        var outputType: TypeDescriptor = TypeDescriptor.empty(),
                        var declarationType: HtlVariableDeclaration? = null,
                        var chain: List<CallChainElement> = listOf()) {
  /**
   * Build call chain segment.
   *
   * @return call chain segment instance
   */
  fun build() = BaseCallChainSegment(
      inputType,
      outputType,
      declarationType,
      chain
  )

}


/**
 * Call chain segment builder entry method.
 *
 * @param init
 *          initialization method
 * @return call chain segment instance
 */
fun chainSegment(init: SegmentDataHolder.() -> Unit): CallChainSegment {
  val dataHolder = SegmentDataHolder()

  init.invoke(dataHolder)

  return dataHolder.build()
}
