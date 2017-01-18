package com.aemtools.completion.htl.callchain

import com.aemtools.completion.htl.callchain.elements.CallChain
import com.aemtools.completion.htl.callchain.elements.CallChainSegment
import com.aemtools.completion.htl.callchain.rawchainprocessor.RawCallChainProcessor
import com.aemtools.completion.htl.model.ResolutionResult
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
        val rawCallChain = propertyAccessMixin.callChain()

        val callChainElements = ArrayList<CallChainSegment>()

        val firstRawCallChainUnit = rawCallChain.pop()
        val rawChainProcessor = RawCallChainProcessor.forRawChain(firstRawCallChainUnit)

        return rawChainProcessor?.processChain(rawCallChain)
    }

}

