package com.aemtools.blocks

import com.aemtools.blocks.fixture.JdkProjectDescriptor
import com.intellij.testFramework.LightProjectDescriptor

/**
 * @author Dmytro Troynikov
 */
abstract class BaseVariantsWithJdkContributorTest(dataPath: String)
  : BaseVariantsCheckContributorTest(dataPath) {

  override fun getProjectDescriptor(): LightProjectDescriptor
      = JdkProjectDescriptor()

}
