package com.aemtools.analysis.htl.resolution

import com.aemtools.analysis.htl.callchain.elements.virtual.BaseVirtualCallChainElement
import com.aemtools.analysis.htl.callchain.elements.virtual.VirtualCallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.mayBeIteratedUpon
import com.intellij.codeInsight.lookup.LookupElement

/**
 * @author Dmytro Troynikov
 */
object VirtualChainResolver {

  private val OVERFLOW_GUARD = 5

  /**
   * Resolve nested iterables starting from given
   * [TypeDescriptor].
   *
   * @param original start point type
   * @return lookup elements
   */
  fun nestedIterables(original: VirtualCallChainElement) =
      resolve(original) { type.mayBeIteratedUpon() }

  /**
   * Resolve call chain by given condition.
   * All sub-elements will be examined by the condition, those
   * evaluated to *true* will be added as [LookupElement] objects.
   *
   * @param original starting virtual call chain element
   * @param condition end condition
   *
   * @return list of lookup elements
   */
  fun resolve(original: VirtualCallChainElement,
              condition: VirtualCallChainElement.() -> Boolean)
      : List<LookupElement> {
    return innerResolve(original, condition, 1)
  }

  private fun innerResolve(
      original: VirtualCallChainElement,
      condition: VirtualCallChainElement.() -> Boolean,
      depth: Int): List<LookupElement> {
    if (depth >= OVERFLOW_GUARD) {
      return emptyList()
    }
    val result: ArrayList<LookupElement> = ArrayList()

    original.subtypes().apply {
      val matched = filter { condition(it) }

      result.addAll(matched.map { it.toLookupElement() })

      forEach {
        result += innerResolve(it, condition, depth.inc())
      }

    }
    return result
  }

  private fun VirtualCallChainElement.subtypes(): List<VirtualCallChainElement> {
    return type.myVariants().filterNot {
      it.lookupString in listOf<String>(
          "class", "toString", "parallelStream",
          "stream", "toArray"
      )
    }.map {
      val name = it.lookupString
      val type = type.subtype(name)
      BaseVirtualCallChainElement(
          name,
          type,
          this
      )
    }
  }

}

