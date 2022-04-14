package com.aemtools.inspection.html

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BasePlatformLightTest
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import java.io.File

/**
 * Test for [RedundantElInspection].
 *
 * @author Dmytro Primshyts
 */
class RedundantElInspectionTest : BasePlatformLightTest() {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testRedundantElInspection() {
    myFixture.enableInspections(RedundantElInspection())

    myFixture.testInspection(
      "com/aemtools/inspection/html/redundant-el",
      LocalInspectionToolWrapper(RedundantElInspection())
    )
  }

}
