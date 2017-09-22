package com.aemtools.completion.htl.common

import com.aemtools.blocks.fixture.UberJarFixtureMixin
import com.aemtools.lang.htl.lexer.HtlTestCase
import com.intellij.codeInsight.completion.LightFixtureCompletionTestCase
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import java.io.File

/**
 * @author Dmytro Troynikov.
 */
class PredefinedVariablesCompletionTest : LightFixtureCompletionTestCase(),
    UberJarFixtureMixin {

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

  fun doTest(completionChar: Char = '\n') {
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
    myFixture.addUberJar()
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
