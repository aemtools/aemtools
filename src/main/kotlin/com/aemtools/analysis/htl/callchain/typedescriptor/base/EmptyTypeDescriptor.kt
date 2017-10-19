package com.aemtools.analysis.htl.callchain.typedescriptor.base

import com.intellij.codeInsight.lookup.LookupElement

/**
 * @author Dmytro Troynikov
 */
open class EmptyTypeDescriptor : TypeDescriptor {
  override fun isArray(): Boolean = false
  override fun isIterable(): Boolean = false
  override fun isMap(): Boolean = false

  override fun myVariants(): List<LookupElement> = listOf()

  override fun subtype(identifier: String): TypeDescriptor = TypeDescriptor.empty()
}
