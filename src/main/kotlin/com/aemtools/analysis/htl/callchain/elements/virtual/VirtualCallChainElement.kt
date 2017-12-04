package com.aemtools.analysis.htl.callchain.elements.virtual

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.ArrayJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.IterableJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.MapJavaTypeDescriptor
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

