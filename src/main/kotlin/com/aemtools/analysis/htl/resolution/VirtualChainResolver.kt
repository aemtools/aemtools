package com.aemtools.analysis.htl.resolution

import com.aemtools.analysis.htl.callchain.elements.BaseVirtualCallChainElement
import com.aemtools.analysis.htl.callchain.elements.VirtualCallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.mayBeIteratedUpon
import com.intellij.codeInsight.lookup.LookupElement

/**
 * @author Dmytro Troynikov
 */
object VirtualChainResolver {

  /**
   * Resolve nested iterables starting from given
   * [TypeDescriptor].
   *
   * @param original start point type
   * @return lookup elements
   */
  fun nestedIterables(original: VirtualCallChainElement) =
      resolve(original) { type.mayBeIteratedUpon() }

  fun resolve(original: VirtualCallChainElement, condition: VirtualCallChainElement.() -> Boolean)
      : List<LookupElement> {
    val result: ArrayList<LookupElement> = ArrayList()

    original.subtypes().apply {
      val matched = filter { condition(it) }

      result.addAll(matched.map { it.toLookupElement() })

      forEach {
        result += resolve(it, condition)
      }

    }
    return result
  }

}

private fun VirtualCallChainElement.subtypes(): List<VirtualCallChainElement> {
  return type.myVariants().map {
    val name = it.lookupString
    val type = type.subtype(name)
    BaseVirtualCallChainElement(
        name,
        type,
        this
    )
  }
}
