package com.aemtools.completion.htl.common

import com.aemtools.common.constant.const
import com.aemtools.test.BaseVariantsCheckContributorTest.Companion.CUSTOM_MODEL_FIELDS
import com.aemtools.test.BaseVariantsCheckContributorTest.Companion.DATA_SLY_SUITABLE_CLASSES
import com.aemtools.test.completion.CompletionBaseLightTest
import com.aemtools.test.fixture.TestClassesMixin

/**
 * Tests for Htl variables resolution.
 *
 * @author Dmytro Primshyts
 */
class HtlVariablesCommonTest : CompletionBaseLightTest(true),
    TestClassesMixin {

  fun testDataSlyUseValueNoEl() = completionTest {
    addHtml("test.html", """
      <div data-sly-use.bean="$CARET"></div>
    """)
    shouldContain(DATA_SLY_SUITABLE_CLASSES + const.CLIENTLIB_TEMPLATE)
  }

  fun testDataSlyUseValue() = completionTest {
    addHtml("test.html", """
      <div data-sly-use.bean="$DOLLAR{'$CARET'}"></div>
    """)
    shouldContain(DATA_SLY_SUITABLE_CLASSES + const.CLIENTLIB_TEMPLATE)
  }

  fun testDataSlySetValue() = completionTest {
    addHtml("test.html", """
      <div data-sly-set.bean="$DOLLAR{'$CARET'}"></div>
    """)
    shouldContain(emptyList())
  }

  fun testDataSlyTestValue() = completionTest {
    addHtml("test.html", """
      <div data-sly-test.bean="$DOLLAR{'$CARET'}"></div>
    """)
    shouldContain(emptyList())
  }

  fun testDataSlyUseValueContextString() = completionTest {
    addHtml("test.html", """
      <div data-sly-use.bean="$DOLLAR{'' @ param='$CARET'}">
    """)
    shouldContain(emptyList())
  }

  fun testStringLiteralShouldBeEmpty() = completionTest {
    addHtml("test.html", """
       $DOLLAR{'$CARET'}
    """)
    shouldContain(emptyList())
  }

  fun testDataSlyUseSecondLevelVariableDeclaredInEl() = completionTest {
    addHtml("test.html", """
      <div data-sly-use.bean="$DOLLAR{'com.aemtools.completion.htl.fixtures.classes.CustomSlingModel'}">
        $DOLLAR{bean.$CARET}
      </div>
    """)
    shouldContain(CUSTOM_MODEL_FIELDS)
  }

  fun testDataSlyUseSecondLevelVariableDeclaredInElWithOption() = completionTest {
    addHtml("test.html", """
      <div data-sly-use.bean="$DOLLAR{'com.aemtools.completion.htl.fixtures.classes.CustomSlingModel' @ option='test'}">
        $DOLLAR{bean.$CARET}
      </div>
    """)
    shouldContain(CUSTOM_MODEL_FIELDS)
  }

  fun testDataSlyUseWithOption() = completionTest {
    addHtml("test.html", """
      <div data-sly-use.bean="$DOLLAR{'com.aemtools.completion.htl.fixtures.classes.CustomSlingModel'
            @ opt=properties}">
        $DOLLAR{bean.$CARET}
      </div>
    """)
    shouldContain(CUSTOM_MODEL_FIELDS)
  }

  override fun setUp() {
    super.setUp()
    myFixture.addClasses()
  }

}
