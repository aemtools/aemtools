package com.aemtools.completion.htl.callchain.elements

/**
 * Base Call Chain class.
 * @author Dmytro_Troynikov
 */
abstract class CallChainElement() {

}

class CallChainEntryElement : CallChainElement()

class RegularChainElement : CallChainElement()

class ArrayAccessChainElement : CallChainElement()

class ListAccessChainElement : CallChainElement()

class MapAccessChainElement : CallChainElement()