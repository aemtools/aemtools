package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.test.util.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

/**
 * Test for [HtlStringLiteralUtils].
 *
 * @author Dmytro Primshyts
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
  fun `toDoubleQuoted should not apply double escape`() {
    val literal = literal("""
       'shouldn\'t \" escape'
    """)

    val result = literal.toDoubleQuoted()

    assertThat(result)
        .isEqualTo("\"shouldn't \\\" escape\"")
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
  fun `toSingleQuoted should not apply double escape`(){
    val literal = literal("""
       "shouldn\'t \" escape"
    """)

    val result = literal.toSingleQuoted()

    assertThat(result)
        .isEqualTo("'shouldn\\'t \" escape'")
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
