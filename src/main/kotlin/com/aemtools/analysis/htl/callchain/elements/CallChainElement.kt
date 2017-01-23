package com.aemtools.analysis.htl.callchain.elements

import com.aemtools.analysis.htl.callchain.typedescriptor.ArrayTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.ListTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.MapTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
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

interface IArrayChainElement : CallChainElement {
    override val type: ArrayTypeDescriptor
}

interface IListChainElement : CallChainElement {
    override val type: ListTypeDescriptor
}

interface IMapChainElement : CallChainElement {
    override val type: MapTypeDescriptor
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

class ArrayChainElement(element: PsiElement,
                        name: String,
                        override val type: ArrayTypeDescriptor)
    : BaseChainElement(element, name, type), IArrayChainElement

class ListChainElement(element: PsiElement,
                       name: String,
                       override val type: ListTypeDescriptor)
    : BaseChainElement(element, name, type), IListChainElement

class MapChainElement(element: PsiElement,
                      name: String,
                      override val type: MapTypeDescriptor)
    : BaseChainElement(element, name, type), IMapChainElement