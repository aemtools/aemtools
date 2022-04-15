package com.aemtools.inspection.html.fix

import com.aemtools.inspection.html.RedundantElInspection
import com.aemtools.test.fix.BaseFixTest

/**
 * Test for [RemoveRedundantElAction] & [RedundantElInspection].
 *
 * @author Dmytro Primshyts
 */
class RemoveRedundantElFixTest : BaseFixTest() {

  fun testUse() = fixTest {
    inspection = RedundantElInspection::class.java
    fixName = "Remove redundant expression."

    before = html("test.html", """
        <div data-sly-use.bean="$DOLLAR{'com.test.${CARET}Bean'}"></div>
    """)

    after = html("test.html", """
        <div data-sly-use.bean="com.test.Bean"></div>
    """)
  }

  fun testInclude() = fixTest {
    inspection = RedundantElInspection::class.java
    fixName = "Remove redundant expression."

    before = html("test.html", """
        <div data-sly-include="$DOLLAR{'path/to/${CARET}include'}"></div>
    """)

    after = html("test.html", """
        <div data-sly-include="path/to/include"></div>
    """)
  }
}
