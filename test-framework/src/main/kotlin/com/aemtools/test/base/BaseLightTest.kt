package com.aemtools.test.base

import com.aemtools.test.base.model.fixture.ITestFixture
import com.aemtools.test.base.model.fixture.TestFixture
import com.aemtools.test.fixture.JdkProjectDescriptor
import com.aemtools.test.fixture.UberJarFixtureMixin
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.LanguageLevelProjectExtension
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.pom.java.LanguageLevel
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.CodeInsightTestFixture.CARET_MARKER
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import java.io.File

/**
 * @author Dmytro Primshyts
 */
abstract class BaseLightTest(private val withAemUberJar: Boolean = false)
  : LightJavaCodeInsightFixtureTestCase(),
    UberJarFixtureMixin {

  val fixture: JavaCodeInsightTestFixture
    get() = super.myFixture

  public override fun getProject(): Project {
    return super.getProject()
  }

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return JdkProjectDescriptor();
  }

  fun fileCase(case: ITestFixture.() -> Unit) {
    val fixture = TestFixture(myFixture)

    case.invoke(fixture)

    fixture.init()

    fixture.test()
  }

  override fun setUp() {
    super.setUp()
    LanguageLevelProjectExtension.getInstance(project).languageLevel = LanguageLevel.JDK_11
    VfsRootAccess.allowRootAccess(myFixture.testRootDisposable, File("src/test").absolutePath)
    VfsRootAccess.allowRootAccess(myFixture.projectDisposable, File("src/test").absolutePath)

    if (withAemUberJar) {
      myFixture.addUberJar()
    }
  }

  override fun tearDown() {
    super.tearDown()
    //VfsRootAccess.disallowRootAccess(File("src/test").absolutePath)
  }

  companion object {
    const val DOLLAR: String = "$"
    const val CARET: String = CARET_MARKER
  }

}
