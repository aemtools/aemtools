package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin

/**
 * Convert current string literal to singlequted string.
 *
 * @receiver [HtlStringLiteralMixin]
 * @return singlequoted version of string literal
 */
fun HtlStringLiteralMixin.toSingleQuoted(): String {
  return if (this.isDoubleQuoted()) {
    text
        // "input ' \" " -> 'input \' " '
        // escape singlequotes inside the literal
        .replace("\\'", "'")
        .replace("'", "\\'")

        // unescape doublequotes inside the literal
        .replace("\\\"", "\"")

        // swap quotes
        .replaceFirst("\"", "'")
        .replaceLast("\"", "'")
  } else {
    text
  }
}

/**
 * Convert current string literal to doublequoted string.
 *
 * @receiver [HtlStringLiteralMixin]
 * @return doublequoted version of string literal
 */
fun HtlStringLiteralMixin.toDoubleQuoted(): String {
  return if (!this.isDoubleQuoted()) {
    text
        // 'input " \' ' -> "input \" ' "
        // escape doublequotes inside the literal
        .replace("\\\"", "\"")
        .replace("\"", "\\\"")

        // unescape singlequotes inside the literal
        .replace("\\'", "'")

        // swap quotes
        .replaceFirst("'", "\"")
        .replaceLast("'", "\"")
  } else {
    text
  }
}

/**
 * Swap quotes of current [HtlStringLiteralMixin].
 * Will return doublequoted for singlequoted and vice versa.
 *
 * @receiver [HtlStringLiteralMixin]
 * @return string with swapped quote
 */
fun HtlStringLiteralMixin.swapQuotes(): String {
  return if (this.isDoubleQuoted()) {
    toSingleQuoted()
  } else {
    toDoubleQuoted()
  }
}

/**
 * Check if current [HtlStringLiteralMixin] is a doublequoted string.
 *
 * @receiver [HtlStringLiteralMixin]
 * @return *true* if current string is doublequoted, *false* otherwise
 */
fun HtlStringLiteralMixin.isDoubleQuoted(): Boolean {
  return this.text.startsWith("\"")
}

private fun String.replaceLast(oldValue: String, newValue: String, ignoreCase: Boolean = false): String =
    reversed()
        .replaceFirst(oldValue, newValue, ignoreCase)
        .reversed()
