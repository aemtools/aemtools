package com.aemtools.codeinsight.htl.annotator

import com.aemtools.test.base.BaseLightTest

/**
 * @author Dmytro Primshyts
 */
abstract class HtlOptionsAnnotatorTest : BaseLightTest() {

  fun testContext() = testStandardOptionHighlight("context")
  fun testFormat() = testStandardOptionHighlight("format")
  fun testI18n() = testStandardOptionHighlight("i18n")
  fun testJoin() = testStandardOptionHighlight("join")
  fun testScheme() = testStandardOptionHighlight("scheme")
  fun testDomain() = testStandardOptionHighlight("domain")
  fun testPath() = testStandardOptionHighlight("path")
  fun testPrependPath() = testStandardOptionHighlight("prependPath")
  fun testAppendPath() = testStandardOptionHighlight("appendPath")
  fun testSelectors() = testStandardOptionHighlight("selectors")
  fun testAddSelectors() = testStandardOptionHighlight("addSelectors")
  fun testRemoveSelectors() = testStandardOptionHighlight("removeSelectors")
  fun testExtension() = testStandardOptionHighlight("extension")
  fun testSuffix() = testStandardOptionHighlight("suffix")
  fun testPrependSuffix() = testStandardOptionHighlight("prependSuffix")
  fun testAppendSuffix() = testStandardOptionHighlight("appendSuffix")
  fun testQuery() = testStandardOptionHighlight("query")
  fun testAddQuery() = testStandardOptionHighlight("addQuery")
  fun testRemoveQuery() = testStandardOptionHighlight("removeQuery")
  fun testFragment() = testStandardOptionHighlight("fragment")
  fun testTimezone() = testStandardOptionHighlight("timezone")
  fun testLocale() = testStandardOptionHighlight("locale")

  fun testTemplateArgument() = fileCase {
    addHtml("/src/test.html", """
<div <info descr="null">data-sly-use</info>.<weak_warning descr="null">temp</weak_warning>="template.html">
<div <info descr="null">data-sly-call</info>="$DOLLAR{<weak_warning descr="null">temp</weak_warning>.template @ <weak_warning descr="Template Argument">param1</weak_warning>=''}">
$CARET
</div>
</div>
        """)
    addHtml("/src/template.html", """
            <div data-sly-template.template="$DOLLAR{@ param1, param2}">

            </div>
        """)
    verify {
      myFixture.checkHighlighting(true, true, true)
    }
  }

  fun testStandardOptionShouldNotBeConsideredInDataSlyCall() {
    myFixture.configureByText("test.html", """
            <div <info descr="null">data-sly-call</info>="$DOLLAR{ @ context=''}"></div>
        """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun testStandardOptionShouldNotBeConsideredInDataSlyUse() {
    myFixture.configureByText("test.html", """
            <div <info descr="null">data-sly-use</info>.<warning descr="null">bean</warning>="$DOLLAR{'com.test.Bean' @ context=''}"></div>
        """)
    myFixture.checkHighlighting(true, true, true)
  }

  private fun testStandardOptionHighlight(option: String) {
    myFixture.configureByText("test.html", """
            $DOLLAR{'' @ <weak_warning descr="Standard Option">$option</weak_warning>=''}
        """)
    myFixture.checkHighlighting(true, true, true)
  }

}
