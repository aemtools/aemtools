package com.aemtools.test.base

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

/**
 * @author Dmytro Primshyts
 */
abstract class BasePlatformLightTest :
  BasePlatformTestCase() {

  fun fixture(): CodeInsightTestFixture = myFixture

}
