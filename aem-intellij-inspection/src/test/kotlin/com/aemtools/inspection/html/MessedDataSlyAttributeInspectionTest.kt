package com.aemtools.inspection.html

import com.aemtools.test.HtlTestCase
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import com.intellij.testFramework.InspectionTestCase
import java.io.File

/**
 * Test for [MessedDataSlyAttributeInspection].
 *
 * @author Dmytro Primshyts
 */
class MessedDataSlyAttributeInspectionTest : InspectionTestCase() {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testInspectionForStyle() {
    doTest("com/aemtools/inspection/html/messed-data-sly-attribute/style",
        LocalInspectionToolWrapper(MessedDataSlyAttributeInspection()))
  }

  fun testInspectionForOnclick() {
    doTest("com/aemtools/inspection/html/messed-data-sly-attribute/onclick",
        LocalInspectionToolWrapper(MessedDataSlyAttributeInspection()))
  }

}
