package com.aemtools.test.base

import com.aemtools.test.base.model.fixture.ITestFixture
import com.aemtools.test.base.model.fixture.TestFixture
import com.aemtools.test.fixture.UberJarFixtureMixin
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.testFramework.fixtures.CodeInsightTestFixture.CARET_MARKER
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import java.io.File

/**
 * @author Dmytro Troynikov
 */
abstract class BaseLightTest(val withAemUberJar: Boolean = false)
  : LightCodeInsightFixtureTestCase(),
    UberJarFixtureMixin {

  val fixture: JavaCodeInsightTestFixture
    get() = super.myFixture

  public override fun getProject(): Project {
    return super.getProject()
  }

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
