package com.aemtools.test

import com.aemtools.test.fixture.JdkProjectDescriptor
import com.intellij.testFramework.LightProjectDescriptor

/**
 * @author Dmytro Primshyts
 */
abstract class BaseVariantsWithJdkContributorTest(dataPath: String)
  : BaseVariantsCheckContributorTest(dataPath) {

  override fun getProjectDescriptor(): LightProjectDescriptor
      = JdkProjectDescriptor()

}
