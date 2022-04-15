package com.aemtools.test.base

import com.aemtools.test.fixture.JdkProjectDescriptor
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

/**
 * @author Dmytro Primshyts
 */
abstract class BasePlatformLightTest :
  BasePlatformTestCase() {

  fun fixture(): CodeInsightTestFixture = myFixture

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return JdkProjectDescriptor()
  }
}
