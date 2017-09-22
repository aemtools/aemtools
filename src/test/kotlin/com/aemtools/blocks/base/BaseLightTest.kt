package com.aemtools.blocks.base

import com.aemtools.blocks.base.model.fixture.ITestFixture
import com.aemtools.blocks.base.model.fixture.TestFixture
import com.aemtools.blocks.fixture.UberJarFixtureMixin
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.testFramework.fixtures.CodeInsightTestFixture.CARET_MARKER
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import java.io.File

/**
 * @author Dmytro Troynikov
 */
abstract class BaseLightTest(val withAemUberJar: Boolean = false)
  : LightCodeInsightFixtureTestCase(),
    UberJarFixtureMixin {

  fun fileCase(case: ITestFixture.() -> Unit) {
    val fixture = TestFixture(myFixture)

    case.invoke(fixture)

    fixture.init()

    fixture.test()
  }

  override fun setUp() {
    super.setUp()
    VfsRootAccess.allowRootAccess(File("src/test").absolutePath)
    if (withAemUberJar) {
      myFixture.addUberJar()
    }
  }

  override fun tearDown() {
    super.tearDown()
    VfsRootAccess.disallowRootAccess(File("src/test").absolutePath)
  }

  companion object {
    val DOLLAR: String = "$"
    val CARET: String = CARET_MARKER
  }

}
