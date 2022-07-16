package com.aemtools.codeinsight.htl.intention

import com.aemtools.common.util.writeCommand
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.util.notNull
import com.aemtools.test.util.quickFix
import org.assertj.core.api.Assertions.assertThat

/**
 * Test for [HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction].
 *
 * @author Dmytro Primshyts
 */
class HtlWrongQuotesXmlAttributeInvertQuotesIntentionActionTest : BaseLightTest() {

  fun testFormat() {
    myFixture.configureByText("test.html", """
      <div attribute="$DOLLAR{"wrong"}"></div>
    """)

    val fix by notNull<HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction> {
      myFixture.quickFix("test.html")
    }

    assertThat(fix.familyName).isEqualTo("HTL Intentions")
    assertThat(fix.text).isEqualTo("Invert XML Attribute quotes")
    assertThat(fix.isAvailable(project, editor, null)).isTrue
  }

  fun testApplyForDoubleQuoted() {
    myFixture.configureByText("test.html", """
      <div attribute="$DOLLAR{"wrong"}"></div>
    """)

    val fix by notNull<HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction> {
      myFixture.quickFix("test.html")
    }

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
      <div attribute='$DOLLAR{"wrong"}'></div>
    """)
  }

  fun testApplyForSingleQuoted() {
    myFixture.configureByText("test.html", """
      <div attribute='$DOLLAR{'wrong'}'></div>
    """)

    val fix by notNull<HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction> {
      myFixture.quickFix("test.html")
    }

    writeCommand(project) {
      fix.invoke(project, editor, file)
    }

    myFixture.checkResult("""
      <div attribute="$DOLLAR{'wrong'}"></div>
    """)
  }

}
