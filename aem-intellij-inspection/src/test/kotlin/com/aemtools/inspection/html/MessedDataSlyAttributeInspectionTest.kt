package com.aemtools.inspection.html

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BasePlatformLightTest
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import java.io.File

/**
 * Test for [MessedDataSlyAttributeInspection].
 *
 * @author Dmytro Primshyts
 */
class MessedDataSlyAttributeInspectionTest : BasePlatformLightTest() {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testInspectionForStyle() {
    myFixture.enableInspections(MessedDataSlyAttributeInspection())

    myFixture.testInspection("com/aemtools/inspection/html/messed-data-sly-attribute/style",
      LocalInspectionToolWrapper(MessedDataSlyAttributeInspection()))
  }

  fun testInspectionForOnclick() {
    myFixture.enableInspections(MessedDataSlyAttributeInspection())

    myFixture.testInspection("com/aemtools/inspection/html/messed-data-sly-attribute/onclick",
      LocalInspectionToolWrapper(MessedDataSlyAttributeInspection()))
  }

}
