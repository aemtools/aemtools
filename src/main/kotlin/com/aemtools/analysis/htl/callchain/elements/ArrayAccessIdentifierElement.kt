package com.aemtools.analysis.htl.callchain.elements

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.intellij.psi.PsiElement

/**
 * Element which represents the array access identifier
 * ```
 *     object[<array access identifier>]
 * ```
 *
 * This class will represent the content of piece of expression between brackets
 * in case if object of Array, List or Map type
 *
 * @author Dmytro Troynikov
 */
class ArrayAccessIdentifierElement(
    override val element: PsiElement
) : BaseChainElement(element, "", TypeDescriptor.empty())
