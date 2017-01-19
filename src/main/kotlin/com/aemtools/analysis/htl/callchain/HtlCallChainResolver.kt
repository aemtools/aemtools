package com.aemtools.analysis.htl.callchain

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.elements.CallChainSegment
import com.aemtools.analysis.htl.callchain.rawchainprocessor.RawCallChainProcessor
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
object HtlCallChainResolver {

    fun resolveCallChain(propertyAccessMixin: PropertyAccessMixin): ResolutionResult? {
        val callChain = callChain(propertyAccessMixin)
        return null
    }

    private fun callChain(propertyAccessMixin: PropertyAccessMixin): CallChain? {
        val rawCallChain: LinkedList<RawChainUnit> = propertyAccessMixin.callChain()

        val callChainElements = ArrayList<CallChainSegment>()

        val firstRawCallChainUnit = rawCallChain.pop()
        val rawChainProcessor = RawCallChainProcessor.forRawChain(rawCallChain)

        return rawChainProcessor?.processChain(rawCallChain)
    }

}

