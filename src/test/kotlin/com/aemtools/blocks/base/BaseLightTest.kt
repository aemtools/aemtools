package com.aemtools.blocks.base

import com.aemtools.blocks.base.model.fixture.ITestFixture
import com.aemtools.blocks.base.model.fixture.TestFixture
import com.intellij.testFramework.fixtures.CodeInsightTestFixture.CARET_MARKER
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase

/**
 * @author Dmytro Troynikov
 */
abstract class BaseLightTest : LightCodeInsightFixtureTestCase() {

    fun fileCase(case: ITestFixture.() -> Unit) {
        val fixture = TestFixture(myFixture)

        case.invoke(fixture)

        fixture.init()

        fixture.test()
    }

    companion object {
        val DOLLAR : String = "$"
        val CARET: String = CARET_MARKER
    }

}