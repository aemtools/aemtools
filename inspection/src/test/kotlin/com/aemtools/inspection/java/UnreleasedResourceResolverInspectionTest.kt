package com.aemtools.inspection.java

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JdkProjectDescriptor
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import com.intellij.testFramework.LightProjectDescriptor
import java.io.File

/**
 * Test for [UnreleasedResourceResolverInspection].
 *
 * @author Dmytro Troynikov
 */
class UnreleasedResourceResolverInspectionTest : BaseLightTest(true) {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return JdkProjectDescriptor()
  }

  fun testUnreleasedResourceResolverInspection() {
    myFixture.enableInspections(UnreleasedResourceResolverInspection())

    myFixture.testInspection("com/aemtools/inspection/java/unreleased-resource-resolver",
        LocalInspectionToolWrapper(UnreleasedResourceResolverInspection()))
  }

}
