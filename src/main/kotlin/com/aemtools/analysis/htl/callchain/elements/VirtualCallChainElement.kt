package com.aemtools.analysis.htl.callchain.elements

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

/**
 * Virtual call chain element interface.
 * Represents call chain that has no physical presentation
 * inside PSI
 * @author Dmytro Troynikov
 */
interface VirtualCallChainElement {
  /**
   * Name of current call chain element.
   */
  val name: String
  /**
   * Type descriptor of current call chain element.
   */
  val type: TypeDescriptor

  /**
   * Previous virtual call chain element from current call chain.
   */
  val previous: VirtualCallChainElement?

  /**
   * Convert current virtual call chain element into [LookupElement].
   *
   * @return lookup element
   */
  fun toLookupElement(): LookupElement
}

/**
 * Base implementation of [VirtualCallChainElement].
 *
 * @author Dmytro Primshyts
 */
class BaseVirtualCallChainElement(
    override val name: String,
    override val type: TypeDescriptor,
    override val previous: VirtualCallChainElement? = null
) : VirtualCallChainElement {
  override fun toLookupElement(): LookupElement {
    return LookupElementBuilder.create(buildString {
      append(name)

      var _previous = previous
      while (_previous != null) {
        insert(0, "${_previous.name}.")
        _previous = _previous.previous
      }
    })
  }

}
