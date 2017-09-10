package com.aemtools.analysis.htl.callchain.typedescriptor.base

/**
 * @author Dmytro Troynikov
 */
open class NamedTypeDescriptor(private val myName: String)
    : EmptyTypeDescriptor() {

    override fun name(): String = myName

}
