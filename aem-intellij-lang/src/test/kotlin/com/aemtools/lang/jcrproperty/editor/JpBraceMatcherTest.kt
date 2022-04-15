package com.aemtools.lang.jcrproperty.editor

import com.aemtools.lang.jcrproperty.JcrPropertyLanguage
import com.aemtools.test.action.TypeActionTest
import com.aemtools.test.base.BaseLightTest.Companion.CARET
import com.aemtools.test.base.BasePlatformLightTest
import com.intellij.lang.LanguageBraceMatching
import org.intellij.lang.annotations.Language

/**
 * Test for [JpBraceMatcher].
 *
 * @author Dmytro Primshyts
 */
class JpBraceMatcherTest : BasePlatformLightTest(),
    TypeActionTest {

  fun `test should close brace`() = jpTypeTest(
      CARET,
      "{",
      "{$CARET}"
  )

  fun `test should skip close brace`() = jpTypeTest(
      "{$CARET}",
      "}",
      "{}$CARET"
  )

  fun `test should remove braces`() = jpTypeTest(
      "{$CARET}",
      "\b",
      ""
  )

  fun `test should close bracket`() = jpTypeTest(
      CARET,
      "[",
      "[$CARET]"
  )

  fun `test should skip close bracket`() = jpTypeTest(
      "[$CARET]",
      "]",
      "[]$CARET"
  )

  fun `test should remove brackets`() = jpTypeTest(
      "[$CARET]",
      "\b",
      ""
  )

  private fun jpTypeTest(
      @Language("JcrProperty") before: String,
      type: String,
      @Language("JcrProperty") after: String) =
      typedActionTest(
          ".content.xml",
          """
           <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
                embed="$before" />
           """,
          type,
          """
           <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
                embed="$after" />
           """
      )

  override fun setUp() {
    super.setUp()
    LanguageBraceMatching.INSTANCE.addExplicitExtension(JcrPropertyLanguage, JpBraceMatcher())
  }
}
