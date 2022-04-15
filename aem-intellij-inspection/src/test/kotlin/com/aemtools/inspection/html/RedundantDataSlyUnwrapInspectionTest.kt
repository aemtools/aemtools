package com.aemtools.inspection.html

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BasePlatformLightTest
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import java.io.File

/**
 * Test for [RedundantDataSlyUnwrapInspection].
 *
 * @author Dmytro Primshyts
 */
class RedundantDataSlyUnwrapInspectionTest : BasePlatformLightTest() {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testDataSlyUnwrapInspection() {
    myFixture.enableInspections(RedundantDataSlyUnwrapInspection())

    myFixture.testInspection("com/aemtools/inspection/html/redundant-data-sly-unwrap/positive",
      LocalInspectionToolWrapper(RedundantDataSlyUnwrapInspection()))
  }

}
