package com.aemtools.analysis.htl.callchain.elements.helper

import com.aemtools.analysis.htl.callchain.elements.segment.BaseCallChainSegment
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.elements.segment.CallChainSegment
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration

/**
 * @author Dmytro_Troynikov
 */
class ChainSegmentBuilder() {
    constructor(init: SegmentDataHolder.() -> Unit = {}) : this() {
        init(dataHolder)
    }

    private val dataHolder = SegmentDataHolder()

    /**
     * Build [CallChainSegment] from underlying [SegmentDataHolder].
     *
     * @return call chain segment instance
     */
    fun build(): CallChainSegment {
        return BaseCallChainSegment(
                dataHolder.inputType,
                dataHolder.outputType,
                dataHolder.declarationType,
                dataHolder.chain
        )
    }

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
                            var chain: List<CallChainElement> = listOf())
}

/**
 * Call chain segment builder entry method.
 *
 * @param init
 *          initialization method
 * @return call chain segment instance
 */
fun chainSegment(init: ChainSegmentBuilder.SegmentDataHolder.() -> Unit): CallChainSegment {
    return ChainSegmentBuilder(init).build()
}
