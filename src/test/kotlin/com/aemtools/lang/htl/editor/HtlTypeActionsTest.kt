package com.aemtools.lang.htl.editor

import com.aemtools.lang.htl.file.HtlFileType
import com.aemtools.test.base.BaseLightTest.Companion.CARET
import com.aemtools.test.base.BaseLightTest.Companion.DOLLAR
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase

/**
 * Test for [HtlBraceMatcher], [HtlQuoteHandler], [HtlBackspaceHandler]
 *
 * @author Dmytro Troynikov
 */
class HtlTypeActionsTest : LightCodeInsightFixtureTestCase() {

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

  fun htlTypeTest(before: String, type: String, result: String)
    = typedActionTest("test.html", before, type, result)

  fun typedActionTest(fileName: String,
                      before: String,
                      type: String,
                      result: String) {
    myFixture.configureByText(fileName, before)
    myFixture.type(type)
    myFixture.checkResult(result)
  }

}
