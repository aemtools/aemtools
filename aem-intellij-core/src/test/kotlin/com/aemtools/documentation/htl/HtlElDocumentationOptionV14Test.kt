package com.aemtools.documentation.htl

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for HTL options documentation for HTL v 1.4.
 *
 * @author Kostiantyn Diachenko
 */
class HtlElDocumentationOptionV14Test : HtlElDocumentationOptionTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }

  fun testDataSlyResourceWcmModeOption() = docCase {
    dataSlyResourceOption("wcmmode")

    documentation("""changes the WCM mode""")
  }

  fun testDataSlyResourceDecorationTagNameOption() = docCase {
    dataSlyResourceOption("decorationTagName")

    documentation("""wraps included resources with tag""")
  }

  fun testDataSlyResourceCssClassNameOption() = docCase {
    dataSlyResourceOption("cssClassName")

    documentation("""adds CSS class to the element""")
  }

  fun testDataSlyListBeginOption() = docCase {
    dataSlyListOption("begin")

    documentation("""iteration begins at the item located at the specified index; first item of the collection has index 0""")
  }

  fun testDataSlyListStepOption() = docCase {
    dataSlyListOption("step")

    documentation("""iteration will only process every step items of the collection, starting with the first one""")
  }

  fun testDataSlyListEndOption() = docCase {
    dataSlyListOption("end")

    documentation("""iteration ends at the item located at the specified index (inclusive)""")
  }

  fun testDataSlyRepeatBeginOption() = docCase {
    dataSlyRepeatOption("begin")

    documentation("""iteration begins at the item located at the specified index; first item of the collection has index 0""")
  }

  fun testDataSlyRepeatStepOption() = docCase {
    dataSlyRepeatOption("step")

    documentation("""iteration will only process every step items of the collection, starting with the first one""")
  }

  fun testDataSlyRepeatEndOption() = docCase {
    dataSlyRepeatOption("end")

    documentation("""iteration ends at the item located at the specified index (inclusive)""")
  }
}
