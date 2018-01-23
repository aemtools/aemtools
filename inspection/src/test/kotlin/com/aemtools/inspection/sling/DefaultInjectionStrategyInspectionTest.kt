package com.aemtools.inspection.sling

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JdkProjectDescriptor
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import com.intellij.testFramework.LightProjectDescriptor
import java.io.File

/**
 * Test for [DefaultInjectionStrategyInspection].
 *
 * @author Dmytro Troynikov
 */
class DefaultInjectionStrategyInspectionTest : BaseLightTest() {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testDefaultInjectionStrategyInspection() {
    myFixture.enableInspections(DefaultInjectionStrategyInspection())

    myFixture.testInspection("com/aemtools/inspection/sling/default-injection",
        LocalInspectionToolWrapper(DefaultInjectionStrategyInspection()))
  }

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return JdkProjectDescriptor()
  }

}
