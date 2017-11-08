package com.aemtools.test.completion

import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.completion.model.CompletionTestFixture
import com.aemtools.test.completion.model.ICompletionTestFixture
import com.aemtools.test.fixture.JdkProjectDescriptor
import com.intellij.testFramework.LightProjectDescriptor

/**
 * @author Dmytro Troynikov
 */
abstract class CompletionBaseLightTest(withUberJar: Boolean = true) : BaseLightTest(withUberJar) {

  fun completionTest(fixture: ICompletionTestFixture.() -> Unit) {
    val completionTestFixture = CompletionTestFixture(myFixture).apply { fixture() }

    completionTestFixture.init()

    completionTestFixture.test()
  }

  override fun getProjectDescriptor(): LightProjectDescriptor {
    return JdkProjectDescriptor()
  }

}
