package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.analysis.htl.callchain.typedescriptor.base.EmptyTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.model.ResolutionResult
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement

/**
 * Type descriptor compounded from several type descriptors.
 *
 * @author Dmytro Troynikov
 */
class MergedTypeDescriptor(vararg val types: TypeDescriptor) : TypeDescriptor {
  override fun myVariants(): List<LookupElement> =
      types.flatMap { it.myVariants() }

  override fun documentation(): String? =
      types.map { it.documentation() }
          .filterNotNull()
          .firstOrNull()

  override fun referencedElement(): PsiElement? =
      types.map { it.referencedElement() }
          .filterNotNull()
          .firstOrNull()

  override fun subtype(identifier: String): TypeDescriptor =
      types.map { it.subtype(identifier) }
          .find { it !is EmptyTypeDescriptor }
          ?: EmptyTypeDescriptor()

  override fun isArray(): Boolean = types.any { it.isArray() }

  override fun isIterable(): Boolean = types.any { it.isIterable() }
  override fun isMap(): Boolean = types.any { it.isMap() }

  override fun asResolutionResult(): ResolutionResult = types
      .map { it.asResolutionResult() }
      .reduce { acc, next -> acc + next }

  /**
   * Plus operator function for [MergedTypeDescriptor].
   * Will append given type descriptor into the end of current type descriptor.
   *
   * @param other type descriptor to append to current
   * @return merged type descriptor
   */
  operator fun plus(other: TypeDescriptor): TypeDescriptor =
      MergedTypeDescriptor(*this.types, other)

}
