package com.aemtools.inspection.html

import com.aemtools.inspection.fix.SubstituteWithRawAttributeIntentionAction
import com.aemtools.test.base.BaseLightTest.Companion.CARET
import com.aemtools.test.base.BaseLightTest.Companion.DOLLAR
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import com.intellij.testFramework.InspectionFixtureTestCase
import org.assertj.core.api.Assertions.assertThat

/**
 * Test for [MessedDataSlyAttributeInspection].
 *
 * @author Dmytro Troynikov
 */
class MessedDataSlyAttributeInspectionTest : InspectionFixtureTestCase() {

  fun testCorrectFormat() {
    myFixture.configureByText("test.html", """
      <div data-sly-attribute.${CARET}style="$DOLLAR{'...'}">
    """)

    val fix by notNull<SubstituteWithRawAttributeIntentionAction> {
      myFixture.quickFix("test.html")
    }

    assertThat(fix.familyName)
        .isEqualTo("HTL")
  }

}
