package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Test for [HtlStringLiteralUtils].
 *
 * @author Dmytro Troynikov
 */
class HtlStringLiteralUtilsTest {

  @Test
  fun testToDoubleQuoted() {
    val literal = literal("""
       '\' "'
    """)

    val result = literal.toDoubleQuoted()

    assertThat(result)
        .isEqualTo("\"' \\\"\"")
  }

  @Test
  fun testToDoubleQuoted2() {
    val literal = literal("""
       "should not change"
    """)

    val result = literal.toDoubleQuoted()

    assertThat(result)
        .isEqualTo("\"should not change\"")
  }

  @Test
  fun testToSingleQuoted() {
    val literal = literal("""
       "' \" \b\t"
    """)

    val result = literal.toSingleQuoted()

    assertThat(result)
        .isEqualTo("'\\' \" \\b\\t'")
  }

  @Test
  fun testToSingleQuoted2() {
    val literal = literal("""
      '\' "'
    """)

    val result = literal.toSingleQuoted()

    assertThat(result)
        .isEqualTo("'\\' \"'")
  }

  @Test
  fun testSwap() {
    val literal = literal("""
       ""
    """)

    val result = literal.swapQuotes()

    assertThat(result)
        .isEqualTo("''")
  }

  @Test
  fun testSwap2() {
    val literal = literal("""
       ''
    """)

    val result = literal.swapQuotes()

    assertThat(result)
        .isEqualTo("\"\"")
  }

  private fun literal(text: String) : HtlStringLiteralMixin
    = mock {
    on(it.text)
        .thenReturn(text.trim())
  }

}
