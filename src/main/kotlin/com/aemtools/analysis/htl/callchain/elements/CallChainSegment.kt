package com.aemtools.analysis.htl.callchain.elements

import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.ArrayJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.IterableJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.MapJavaTypeDescriptor
import com.aemtools.completion.htl.model.HtlVariableDeclaration
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.constant.const.IDEA_STRING_CARET_PLACEHOLDER

/**
 * @Author Dmytro_Troynikov
 */
interface CallChainSegment {

    /**
     * [TypeDescriptor] for output type.
     */
    fun outputType(): TypeDescriptor

    /**
     * [TypeDescriptor] for input type.
     */
    fun inputType(): TypeDescriptor

    /**
     * List of [CallChainElement] items.
     */
    fun chainElements(): List<CallChainElement>

    companion object {
        private val EMPTY = EmptyCallChainSegment()
        fun empty(): CallChainSegment = EMPTY
    }
}

class EmptyCallChainSegment : CallChainSegment {
    override fun outputType(): TypeDescriptor = TypeDescriptor.empty()

    override fun inputType(): TypeDescriptor = TypeDescriptor.empty()

    override fun chainElements(): List<CallChainElement> = listOf()
}

class BaseCallChainSegment(
        private val input: TypeDescriptor,
        private val output: TypeDescriptor,
        val declaration: HtlVariableDeclaration?,
        private val elements: List<CallChainElement>) : CallChainSegment {
    override fun inputType(): TypeDescriptor = input

    override fun outputType(): TypeDescriptor = output

    override fun chainElements(): List<CallChainElement> = elements
}

fun List<CallChainElement>.resolveSelectedItem(): ResolutionResult {

    if (this.isEmpty()) {
        return ResolutionResult()
    }

    val selectedItem = this.find { it.name.contains(IDEA_STRING_CARET_PLACEHOLDER) }
    val indexOfSelectedItem = this.indexOf(selectedItem)

    return when {
        this.size > 2
                && this[indexOfSelectedItem - 1] is ArrayAccessIdentifierElement ->
            with(this[indexOfSelectedItem - 2].type) {
                when {
                    this is ArrayJavaTypeDescriptor ->
                        this.arrayType().asResolutionResult()
                    this is IterableJavaTypeDescriptor ->
                        this.iterableType().asResolutionResult()
                    this is MapJavaTypeDescriptor ->
                        this.valueType().asResolutionResult()
                    else -> this.asResolutionResult()
                }
            }
        else -> this[indexOfSelectedItem - 1].type.asResolutionResult()
    }

}