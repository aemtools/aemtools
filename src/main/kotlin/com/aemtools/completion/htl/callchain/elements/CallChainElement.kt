package com.aemtools.completion.htl.callchain.elements

import com.aemtools.completion.htl.callchain.typedescriptor.ArrayTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.ListTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.MapTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.TypeDescriptor
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

/**
 * Compound access chain elements holds about information about such constructions as
 *
 * ```
 *   properties[identifier|literal]
 * ```
 */
abstract class CompoundAccessChainElement(val left: CallChainElement, val right: CallChainElement) :
        CallChainElement

/**
 * Regular compound element describes such relation between elements when left element
 * is neither `List`, `Array` nor `Map`, in that case right element is the provider of name of identifier for left elmeent.
 */
class RegularCompoundAccessChainElement(
        left: CallChainElement,
        right: CallChainElement
) : CompoundAccessChainElement(left, right), CallChainElement by right

/**
 * Left element is an array. In that case right element should be resolved to `Integer`.
 */
class ArrayAccessChainElement(left: ArrayChainElement, right: CallChainElement)
    : CompoundAccessChainElement(left, right), IArrayChainElement by left

/**
 * Left element is an List inheritor. In that case right element should be resolved to `Integer`.
 */
class ListAccessChainElement(left: ListChainElement, right: CallChainElement)
    : CompoundAccessChainElement(left, right), IListChainElement by left

/**
 * Left element is an Map inheritor. In that case right element should be resolved to type of left elements key type.
 */
class MapAccessChainElement(left: MapChainElement, right: CallChainElement)
    : CompoundAccessChainElement(left, right), IMapChainElement by left