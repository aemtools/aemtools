package com.aemtools.analysis.htl.callchain.rawchainprocessor

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.completion.htl.model.DataSlyUseType
import com.aemtools.completion.util.dataSlyUseType
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
interface RawCallChainProcessor {

    fun processChain(rawChain: LinkedList<RawChainUnit>)
            : CallChain

    companion object {
        fun forRawChain(rawChain: List<RawChainUnit>): RawCallChainProcessor? {
            val xmlAttribute = rawChain.myDeclaration?.xmlAttribute ?: return null



            return when (xmlAttribute.dataSlyUseType()) {
                DataSlyUseType.JAVA -> JavaRawCallChainProcessor
                DataSlyUseType.JAVASCRIPT -> JavascriptRawCallChainProcessor
                else -> null
            }
        }

    }

}
