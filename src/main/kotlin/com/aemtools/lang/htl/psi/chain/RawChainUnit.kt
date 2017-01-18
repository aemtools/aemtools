package com.aemtools.lang.htl.psi.chain

import com.aemtools.completion.htl.model.HtlVariableDeclaration
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
data class RawChainUnit(val myCallChain: LinkedList<PsiElement>,
                        val myDeclaration: HtlVariableDeclaration? = null) {
    /**
     * Check if current unit has [HtlVariableDeclaration]
     * The existence of the declaration means that the unit is part of
     * "declaration" HTL attribute (e.g. data-sly-use or data-sly-test)
     */
    fun  hasDeclaration(): Boolean = myDeclaration != null

    /**
     * Check if current unit has [com.aemtools.completion.htl.model.ResolutionResult].
     * The presence of resolution result would mean that the chain unit was resolved
     * during raw chain construction.
     */
    fun hasResolution(): Boolean = myDeclaration?.resolutionResult != null

    /**
     * Check if current unit has predefined completion variants.
     */
    fun hasPredefinedVariants(): Boolean = myDeclaration?.resolutionResult?.predefined != null

}