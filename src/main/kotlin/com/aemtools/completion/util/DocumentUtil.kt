package com.aemtools.completion.util

import com.intellij.openapi.editor.Document
import com.intellij.util.text.CharArrayUtil

/**
 * Method search text by the position.
 *
 * @param text Input text
 * @param offset Position for searching
 *
 * @receiver [Document]
 * @return true when text was found on this position and false otherwise
 */
fun Document.hasText(text: String, offset: Int): Boolean =
    CharArrayUtil.regionMatches(this.charsSequence, offset, text)
