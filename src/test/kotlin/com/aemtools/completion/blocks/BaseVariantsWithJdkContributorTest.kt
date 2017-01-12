package com.aemtools.completion.blocks

import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.testFramework.LightProjectDescriptor
import java.io.File

/**
 * @author Dmytro Troynikov
 */
abstract class BaseVariantsWithJdkContributorTest(dataPath: String) : BaseVariantsCheckContributorTest(dataPath) {

    override fun getProjectDescriptor(): LightProjectDescriptor {
        return object : LightProjectDescriptor() {
            override fun getSdk(): Sdk? {
                val javaHome = System.getProperty("java.home")
                assert(File(javaHome).isDirectory)
                return JavaSdk.getInstance().createJdk("Full JDK", javaHome, true)
            }
        }
    }

}