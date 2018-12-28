package com.aemtools.inspection.html

import com.aemtools.test.HtlTestCase
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import com.intellij.testFramework.InspectionTestCase
import java.io.File

/**
 * Test for [RedundantDataSlyUnwrapInspection].
 *
 * @author Dmytro Primshyts
 */
class RedundantDataSlyUnwrapInspectionTest : InspectionTestCase() {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testDataSlyUnwrapInspection() {
    doTest("com/aemtools/inspection/html/redundant-data-sly-unwrap/positive",
        LocalInspectionToolWrapper(RedundantDataSlyUnwrapInspection()))
  }

}
