package com.aemtools.completion.htl.common

import com.aemtools.lang.htl.lexer.HtlTestCase
import com.intellij.codeInsight.completion.LightFixtureCompletionTestCase
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import java.io.File

/**
 * @author Dmytro Troynikov.
 */
class PredefinedVariablesCompletionTest : LightFixtureCompletionTestCase() {

    fun testCompleteStartedVariable() = doTest()

    fun testSecondLevelCompletion() = doTest()

    fun testThirdLevelCompletion() = doTest()

    fun testCompletionWithinArray() = doTest()

    fun testCompletionWithinTernaryOperation() = doTest()

    fun testCompletionWithinContext() = doTest()
    fun testCompletionWithContextPresent() = doTest()

    fun testDataSlyUseVariable() = doTest()

    fun testDataSlyUseSecondLevel() = doTest()

    fun testDataSlyUseSecondLevelResolvedFromEL() = doTest()

    fun testDataSlyTestVariable() = doTest()

    fun testCompletionWithPredefinedValue() = doTest()

    fun testArrayLikeAccessSinglequoted() = doTest()
    fun testArrayLikeAccessDoublequoted() = doTest()

    fun doTest(completionChar: Char = '\n', vararg extraFileNames: String) {
        val fileName = getTestName(true)

        configureByFile("$fileName.html")
        complete()
        if (myItems != null) {
            val item = myItems.singleOrNull()
            if (item != null) {
                selectItem(item, completionChar)
            }
        }

        checkResultByFile("$fileName.html.after")
    }

    val fixture: JavaCodeInsightTestFixture
        get() = myFixture

    override fun setUp() {
        super.setUp()
        VfsRootAccess.allowRootAccess(File("src/test").absolutePath)
        PsiTestUtil.addLibrary(myModule,
                "aem-api",
                File("src/test/resources/testLibs/").absolutePath,
                "aem-api-6.0.0.1.jar")
    }

    override fun tearDown() {
        super.tearDown()
        VfsRootAccess.disallowRootAccess(File("src/test").absolutePath)
    }

    override fun getTestDataPath() = File("${HtlTestCase.testResourcesPath}/com/aemtools/completion/htl/fixtures/").path + "/"

    override fun getProjectDescriptor(): LightProjectDescriptor {
        return LightCodeInsightFixtureTestCase.JAVA_8
    }

}