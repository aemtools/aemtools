package com.aemtools.analysis.htl.callchain

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.rawchainprocessor.RawCallChainProcessor
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin

/**
 * @author Dmytro_Troynikov
 */
object HtlCallChainResolver {

    fun resolveCallChain(propertyAccessMixin: PropertyAccessMixin): CallChain? =
            RawCallChainProcessor.processChain(propertyAccessMixin.callChain())

}