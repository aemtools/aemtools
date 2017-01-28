package com.aemtools.analysis.htl.callchain.rawchainprocessor

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
object JavascriptRawCallChainProcessor : RawCallChainProcessor {
    override fun processChain(rawChain: LinkedList<RawChainUnit>): CallChain {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}