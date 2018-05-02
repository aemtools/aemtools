package com.aemtools.common.completion

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
