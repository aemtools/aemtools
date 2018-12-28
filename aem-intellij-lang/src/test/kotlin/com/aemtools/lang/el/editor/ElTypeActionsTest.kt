package com.aemtools.lang.el.editor

import com.aemtools.test.action.TypeActionTest
import com.aemtools.test.base.BaseLightTest.Companion.CARET
import com.aemtools.test.base.BaseLightTest.Companion.DOLLAR
import com.aemtools.test.base.BasePlatformLightTest

/**
 * @author Dmytro Primshyts
 */
class ElTypeActionsTest : BasePlatformLightTest(),
    TypeActionTest {

  fun `test should close parentheses`() = elTypeTest(
      "$DOLLAR{$CARET}",
      "(",
      "$DOLLAR{($CARET)}"
  )

  fun `test should skip parentheses`() = elTypeTest(
      "$DOLLAR{($CARET)}",
      ")",
      "$DOLLAR{()$CARET}"
  )

  fun `test should remove parentheses`() = elTypeTest(
      "$DOLLAR{($CARET)}",
      "\b",
      "$DOLLAR{$CARET}"
  )

  fun `test should close bracket`() = elTypeTest(
      "$DOLLAR{$CARET}",
      "[",
      "$DOLLAR{[$CARET]}"
  )

  fun `test should skip bracket`() = elTypeTest(
      "$DOLLAR{[$CARET]}",
      "]",
      "$DOLLAR{[]$CARET}"
  )

  fun `test should remove bracket`() = elTypeTest(
      "$DOLLAR{[$CARET]}",
      "\b",
      "$DOLLAR{$CARET}"
  )

  fun `test should close single quote string`() = elTypeTest(
      "$DOLLAR{$CARET}",
      "'",
      "$DOLLAR{'$CARET'}"
  )

  fun `test should skip single quote string`() = elTypeTest(
      "$DOLLAR{'$CARET'}",
      "\'",
      "$DOLLAR{''$CARET}"
  )

  fun `test should remove single quote string`() = elTypeTest(
      "$DOLLAR{'$CARET'}",
      "\b",
      "$DOLLAR{$CARET}"
  )

  private fun elTypeTest(
      before: String,
      type: String,
      result: String) = typedActionTest(
      ".content.xml",
      """<jcr:root attribute="$before"></jcr:root>""",
      type,
      """<jcr:root attribute="$result"></jcr:root>"""
  )

}
