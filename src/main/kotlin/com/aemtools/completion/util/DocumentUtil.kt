package com.aemtools.completion.util

import com.intellij.openapi.editor.Document
import com.intellij.util.text.CharArrayUtil


fun Document.hasText(text: String, offset: Int): Boolean =
        CharArrayUtil.regionMatches(this.charsSequence, offset, text)
