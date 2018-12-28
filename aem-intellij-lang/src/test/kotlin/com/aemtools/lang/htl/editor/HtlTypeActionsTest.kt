package com.aemtools.lang.htl.editor

import com.aemtools.test.action.TypeActionTest
import com.aemtools.test.base.BaseLightTest

/**
 * Test for [HtlBraceMatcher], [HtlQuoteHandler], [HtlBackspaceHandler]
 *
 * @author Dmytro Primshyts
 */
class HtlTypeActionsTest : BaseLightTest(),
    TypeActionTest {

  fun `test should close expression`() = htlTypeTest(
      "$DOLLAR$CARET",
      "{",
      "$DOLLAR{$CARET}"
  )

  fun `test should skip close brace`() = htlTypeTest(
      "$DOLLAR{$CARET}",
      "}",
      "$DOLLAR{}$CARET"
  )

  fun `test should remove expression`() = htlTypeTest(
      "$DOLLAR{$CARET}",
      "\b",
      ""
  )

  fun `test should close parentheses`() = htlTypeTest(
      "$DOLLAR{$CARET}",
      "(",
      "$DOLLAR{($CARET)}"
  )

  fun `test should skip close parentheses`() = htlTypeTest(
      "$DOLLAR{($CARET)}",
      ")",
      "$DOLLAR{()$CARET}"
  )

  fun `test should remove parentheses`() = htlTypeTest(
      "$DOLLAR{($CARET)}",
      "\b",
      "$DOLLAR{$CARET}"
  )

  fun `test should close bracket`() = htlTypeTest(
      "$DOLLAR{$CARET}",
      "[",
      "$DOLLAR{[$CARET]}"
  )

  fun `test should skip bracket`() = htlTypeTest(
      "$DOLLAR{[$CARET]}",
      "]",
      "$DOLLAR{[]$CARET}"
  )

  fun `test should remove bracket`() = htlTypeTest(
      "$DOLLAR{[$CARET]}",
      "\b",
      "$DOLLAR{$CARET}"
  )

  fun `test should close single quote string`() = htlTypeTest(
      "$DOLLAR{$CARET}",
      "'",
      "$DOLLAR{'$CARET'}"
  )

  fun `test should skip single quote string`() = htlTypeTest(
      "$DOLLAR{'$CARET'}",
      "\'",
      "$DOLLAR{''$CARET}"
  )

  fun `test should remove single quote string`() = htlTypeTest(
      "$DOLLAR{'$CARET'}",
      "\b",
      "$DOLLAR{$CARET}"
  )

  fun `test should close double quote string`() = htlTypeTest(
      "$DOLLAR{$CARET}",
      "\"",
      "$DOLLAR{\"$CARET\"}"
  )

  fun `test should skip double quote string`() = htlTypeTest(
      """$DOLLAR{"$CARET"}""",
      "\"",
      """$DOLLAR{""$CARET}"""
  )

  fun `test should remove double quote string`() = htlTypeTest(
      """$DOLLAR{"$CARET"}""",
      "\b",
      "$DOLLAR{$CARET}"
  )

  private fun htlTypeTest(before: String, type: String, result: String)
      = typedActionTest("test.html", before, type, result)

}
