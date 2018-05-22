package com.aemtools.common.completion

import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

/**
 * @author Dmytro Primshyts
 */

/**
 * Shortcut for
 *
 * ```
 * LookupElementBuilder.create("string")
 * ```
 * invocation
 *
 * @param text completion string
 * @see [LookupElementBuilder]
 * @return lookup element builder
 */
fun lookupElement(text: String) = LookupElementBuilder.create(text)

/**
 * Add priority to current [LookupElement].
 *
 * @param priority the priority
 *
 * @receiver [LookupElement]
 * @return [PrioritizedLookupElement] with given priority
 */
fun LookupElement.withPriority(priority: Double): LookupElement =
    PrioritizedLookupElement.withPriority(this, priority)

/**
 * Get priority of current lookup element if available.
 *
 * @receiver [LookupElement]
 * @see [PrioritizedLookupElement]
 * @return priority of current lookup element,
 * _null_ if current element is not instance of [PrioritizedLookupElement]
 */
fun LookupElement.priority(): Double? = if (this is PrioritizedLookupElement<*>) {
  this.priority
} else {
  null
}

/**
 * Add proximity to current [LookupElement].
 *
 * @param proximity the proximity
 * @receiver [LookupElement]
 * @return [PrioritizedLookupElement] with given proximity
 */
fun LookupElement.withProximity(proximity: Int) =
    PrioritizedLookupElement.withExplicitProximity(this, proximity)
