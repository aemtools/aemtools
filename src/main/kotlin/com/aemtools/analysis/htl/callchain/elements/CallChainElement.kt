package com.aemtools.analysis.htl.callchain.elements

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.intellij.psi.PsiElement

/**
 * Call chain element interface.
 * @author Dmytro_Troynikov
 */
interface CallChainElement {
    val element: PsiElement
    val name: String
    val type: TypeDescriptor
}

open class BaseChainElement(override val element: PsiElement,
                            override val name: String,
                            override val type: TypeDescriptor) : CallChainElement

/**
 * Element which represents the array access identifier
 * ```
 *     object[<array access identifier>]
 * ```
 *
 * This class will represent the content of piece of expression between brackets
 * in case if object of Array, List or Map type
 */
class ArrayAccessIdentifierElement(override val element: PsiElement)
    : BaseChainElement(element, "", TypeDescriptor.empty())
