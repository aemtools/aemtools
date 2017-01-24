package com.aemtools.analysis.htl.callchain

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.rawchainprocessor.JavaRawCallChainProcessor
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
object HtlCallChainResolver {

    fun resolveCallChain(propertyAccessMixin: PropertyAccessMixin): CallChain? {
        return callChain(propertyAccessMixin)
    }

    private fun callChain(propertyAccessMixin: PropertyAccessMixin): CallChain? {
        val rawCallChain: LinkedList<RawChainUnit> = propertyAccessMixin.callChain()

        return JavaRawCallChainProcessor.processChain(rawCallChain)
    }

}