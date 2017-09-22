package com.aemtools.analysis.htl.callchain.typedescriptor.base

import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiUnresolvedTypeDescriptor
import com.aemtools.completion.htl.model.ResolutionResult
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMember

/**
 * Base Type Descriptor.
 */
interface TypeDescriptor {

  /**
   * Return list of available [LookupElement] objects.
   *
   * @return list of lookup elements
   */
  fun myVariants(): List<LookupElement>

  /**
   * Resolve subtype by identifier.
   *
   * @return type of member resolved by identifier, if no subtype available
   * [TypeDescriptor.empty] will be returned.
   */
  fun subtype(identifier: String): TypeDescriptor

  /**
   * Check if current type doesn't have any variants.
   *
   * @return *true* if current type descriptor doesn't have any variants available,
   * *false* otherwise
   */
  fun isEmpty() = myVariants().isEmpty()

  /**
   * Check if current type is not empty.
   * Reverse version of [isEmpty] method.
   *
   * @return *true* if current type descriptor have at least of variant available,
   * *false* otherwise
   */
  fun isNotEmpty() = !isEmpty()

  /**
   * Check if the type is array type.
   *
   * @return *true* if current type descriptor is a holder of "array" type,
   * *false* otherwise
   */
  fun isArray(): Boolean

  /**
   * Check if the type is iterable (i.e. may be used within `data-sly-list` or `data-sly-repeat`).
   *
   * @return *true* if current type descriptor is a holder of type that can be used in
   * `data-sly-list` or `data-sly-repeat`, *false* otherwise
   */
  fun isIterable(): Boolean

  /**
   * Check if the type is a map.
   *
   * @return *true* if current type descriptor represents "map" type, *false* otherwise
   */
  fun isMap(): Boolean

  /**
   * Get description of the type.
   *
   * @return string which may represent the underlying type in IDEA "quick doc" window,
   * the string may contain HTML markup, *null* value would mean that the type doesn't have
   * documentation available
   */
  fun documentation(): String? = null

  /**
   * Get declaration element.
   *
   * @return element - declaration of the type, *null* if no such element available
   */
  fun referencedElement(): PsiElement? = null

  companion object {
    private val EMPTY_DESCRIPTOR = EmptyTypeDescriptor()

    /**
     * Create empty type descriptor.
     *
     * @return instance of empty type descriptor
     */
    fun empty(): TypeDescriptor = EMPTY_DESCRIPTOR

    /**
     * Create "unresolved" type descriptor.
     *
     * @param psiMember the psi member
     * @return instance of [JavaPsiUnresolvedTypeDescriptor]
     */
    fun unresolved(psiMember: PsiMember) = JavaPsiUnresolvedTypeDescriptor(psiMember)
  }

  /**
   * Convert current type descriptor into [ResolutionResult] instance
   * the [#myVariants] result will be propagated as [ResolutionResult#predefined] variants.
   *
   * @return instance of resolution result
   */
  fun asResolutionResult(): ResolutionResult = ResolutionResult(predefined = myVariants())

}
