package com.aemtools.blocks.base

import com.aemtools.blocks.base.model.fixture.ITestFixture
import com.aemtools.blocks.base.model.fixture.TestFixture
import com.aemtools.lang.htl.lexer.HtlTestCase
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.CodeInsightTestFixture.CARET_MARKER
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import java.io.File

/**
 * @author Dmytro Troynikov
 */
abstract class BaseLightTest(val withAemUberJar: Boolean = false) : LightCodeInsightFixtureTestCase() {

    fun fileCase(case: ITestFixture.() -> Unit) {
        val fixture = TestFixture(myFixture)

        case.invoke(fixture)

        fixture.init()

        fixture.test()
    }

    override fun setUp() {
        super.setUp()
        if (withAemUberJar) {
            PsiTestUtil.addLibrary(myModule,
                    "aem-api",
                    File("src/test/resources/testLibs/").absolutePath,
                    "aem-api-6.0.0.1.jar")
        }
    }

    companion object {
        val DOLLAR : String = "$"
        val CARET: String = CARET_MARKER
    }

}