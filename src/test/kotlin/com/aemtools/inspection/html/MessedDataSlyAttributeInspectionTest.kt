package com.aemtools.inspection.html

import com.aemtools.blocks.base.BaseLightTest.Companion.CARET
import com.aemtools.blocks.base.BaseLightTest.Companion.DOLLAR
import com.aemtools.blocks.util.notNull
import com.aemtools.blocks.util.quickFix
import com.aemtools.inspection.fix.SubstituteWithRawAttributeIntentionAction
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
