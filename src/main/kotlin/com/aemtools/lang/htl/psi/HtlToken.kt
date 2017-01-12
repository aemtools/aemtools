package com.aemtools.lang.htl.psi

import com.aemtools.lang.htl.HtlLanguage
import com.intellij.psi.tree.IElementType

/**
 * Created by dmytro on 3/31/16.
 */
class HtlToken(debugName: String)
: IElementType(debugName, HtlLanguage) {
    override fun toString() = "[Htl] ${super.toString()}"
}

class HtlElement(debugName: String)
: IElementType(debugName, HtlLanguage) {
    override fun toString() = "[Htl] ${super.toString()}"
}