package com.aemtools.lang.htl.psi

import com.aemtools.lang.htl.HtlLanguage
import com.intellij.psi.tree.IElementType

/**
* @author Dmytro Troynikov
*/
class HtlToken(debugName: String)
: IElementType(debugName, HtlLanguage) {
    override fun toString() = "[Htl] ${super.toString()}"
}

class HtlElement(debugName: String)
: IElementType(debugName, HtlLanguage) {
    override fun toString() = "[Htl] ${super.toString()}"
}