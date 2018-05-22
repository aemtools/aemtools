package com.aemtools.inspection.java

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JdkProjectDescriptor
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import com.intellij.testFramework.LightProjectDescriptor
import java.io.File

/**
 * Test for [AemConstantInspection].
 *
 * @author Dmytro Troynikov
 */
class AemConstantInspectionTest : BaseLightTest() {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testAemConstantInspection() {
    myFixture.enableInspections(AemConstantInspection())

    myFixture.testInspection("com/aemtools/inspection/java/aem-constant",
        LocalInspectionToolWrapper(AemConstantInspection()))
  }

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return JdkProjectDescriptor()
  }

}
