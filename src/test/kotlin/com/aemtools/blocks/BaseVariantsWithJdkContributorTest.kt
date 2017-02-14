package com.aemtools.blocks

import com.aemtools.blocks.fixture.JdkProjectDescriptor
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.testFramework.IdeaTestUtil
import com.intellij.testFramework.LightProjectDescriptor
import java.io.File

/**
 * @author Dmytro Troynikov
 */
abstract class BaseVariantsWithJdkContributorTest(dataPath: String) : BaseVariantsCheckContributorTest(dataPath) {

    override fun getProjectDescriptor(): LightProjectDescriptor
            = JdkProjectDescriptor()

}