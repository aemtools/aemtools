package com.aemtools.blocks.completion

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.completion.model.CompletionTestFixture
import com.aemtools.blocks.completion.model.ICompletionTestFixture
import com.aemtools.blocks.fixture.JdkProjectDescriptor
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
