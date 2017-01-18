package com.aemtools.completion.htl.callchain.elements

import com.aemtools.completion.htl.callchain.typedescriptor.TypeDescriptor

/**
 * @Author Dmytro_Troynikov
 */
interface CallChainSegment {

    /**
     * [TypeDescriptor] for output type.
     */
    fun outputType() : TypeDescriptor

    /**
     * [TypeDescriptor] for input type.
     */
    fun inputType() : TypeDescriptor

    /**
     * List of [CallChainElement] items.
     */
    fun chainElements() : List<CallChainElement>

    companion object {
        private val EMPTY = EmptyCallChainSegment()
        fun empty(): CallChainSegment = EMPTY
    }
}

class EmptyCallChainSegment : CallChainSegment{
    override fun outputType(): TypeDescriptor = TypeDescriptor.empty()

    override fun inputType(): TypeDescriptor = TypeDescriptor.empty()

    override fun chainElements() : List<CallChainElement> = listOf()
}

class BaseCallChainSegment(
        private val input: TypeDescriptor,
        private val output: TypeDescriptor,
        private val elements: List<CallChainElement>) : CallChainSegment {
    override fun inputType(): TypeDescriptor = input

    override fun outputType(): TypeDescriptor = output

    override fun chainElements(): List<CallChainElement> = elements
}
