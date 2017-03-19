package com.aemtools.lang.htl.psi.chain

import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.intellij.psi.PsiElement
import java.util.*

/**
 * The call chain unit
 * e.g.
 *
 * ```
 *  ${properties.myProperty} -> [properties, myProperty]
 * ```
 *
 * @author Dmytro_Troynikov
 */
open class RawChainUnit(val myCallChain: LinkedList<PsiElement>,
                   val myDeclaration: HtlVariableDeclaration? = null) {
    /**
     * Check if current unit has [HtlVariableDeclaration]
     * The existence of the declaration means that the unit is part of
     * "declaration" HTL attribute (e.g. data-sly-use or data-sly-test)
     */
    fun hasDeclaration(): Boolean = myDeclaration != null

    override fun toString(): String {
        return "RawChainUnit(myCallChain=$myCallChain, myDeclaration=$myDeclaration)"
    }

}