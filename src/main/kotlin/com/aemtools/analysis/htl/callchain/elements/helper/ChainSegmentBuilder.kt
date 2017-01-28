package com.aemtools.analysis.htl.callchain.elements.helper

import com.aemtools.analysis.htl.callchain.elements.BaseCallChainSegment
import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.elements.CallChainSegment
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.completion.htl.model.HtlVariableDeclaration

/**
 * @author Dmytro_Troynikov
 */
class ChainSegmentBuilder() {
    constructor(init: SegmentDataHolder.() -> Unit = {}) : this() {
        init(dataHolder)
    }

    private val dataHolder = SegmentDataHolder()

    fun build(): CallChainSegment {
        return BaseCallChainSegment(
                dataHolder.inputType,
                dataHolder.outputType,
                dataHolder.declarationType,
                dataHolder.chain
        )

        return CallChainSegment.empty()
    }

    class SegmentDataHolder(var inputType: TypeDescriptor = TypeDescriptor.empty(),
                            var outputType: TypeDescriptor = TypeDescriptor.empty(),
                            var declarationType: HtlVariableDeclaration? = null,
                            var chain: List<CallChainElement> = listOf()) {

    }
}

fun chainSegment(init: ChainSegmentBuilder.SegmentDataHolder.() -> Unit): CallChainSegment {
    return ChainSegmentBuilder(init).build()
}