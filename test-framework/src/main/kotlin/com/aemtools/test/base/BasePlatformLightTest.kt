package com.aemtools.test.base

import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

/**
 * @author Dmytro Primshyts
 */
abstract class BasePlatformLightTest :
    LightPlatformCodeInsightFixtureTestCase() {

  fun fixture(): CodeInsightTestFixture = myFixture

}
