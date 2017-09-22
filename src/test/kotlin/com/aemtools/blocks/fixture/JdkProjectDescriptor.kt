package com.aemtools.blocks.fixture

import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.testFramework.LightProjectDescriptor
import java.io.File

/**
 * @author Dmytro Troynikov
 */
class JdkProjectDescriptor : LightProjectDescriptor() {
  override fun getSdk(): Sdk? {
    val javaHome = System.getProperty("java.home")
    assert(File(javaHome).isDirectory)
    return JavaSdk.getInstance().createJdk("Full JDK", javaHome, true)
  }
}
