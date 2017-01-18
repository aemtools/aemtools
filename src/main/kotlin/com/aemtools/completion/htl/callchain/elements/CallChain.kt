package com.aemtools.completion.htl.callchain.elements

import com.aemtools.completion.htl.callchain.typedescriptor.TypeDescriptor

/**
 * @author Dmytro_Troynikov
 */

class CallChain(val callChainSegments: List<CallChainSegment>) {
    companion object {
        private val EMPTY_CHAIN = CallChain(listOf())
        fun empty() = EMPTY_CHAIN
    }
}


