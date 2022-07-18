package com.aemtools.test.base

import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.test.fixture.HtlVersioningFixtureMixin
import com.aemtools.test.fixture.JdkProjectDescriptor
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

/**
 * @author Dmytro Primshyts
 */
abstract class BasePlatformLightTest :
    HtlVersioningFixtureMixin,
    BasePlatformTestCase() {

  fun fixture(): CodeInsightTestFixture = myFixture

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return JdkProjectDescriptor()
  }

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }
}
