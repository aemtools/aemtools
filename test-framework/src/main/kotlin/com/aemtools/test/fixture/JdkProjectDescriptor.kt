package com.aemtools.test.fixture

import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.pom.java.LanguageLevel
import com.intellij.testFramework.IdeaTestUtil
import com.intellij.testFramework.LightProjectDescriptor
import java.io.File

/**
 * @author Dmytro Primshyts
 */
class JdkProjectDescriptor : LightProjectDescriptor() {
  override fun getSdk(): Sdk? {
    //var mockJdk = IdeaTestUtil.getMockJdk(LanguageLevel.JDK_11.toJavaVersion())
    //return mockJdk
    val javaHome = System.getProperty("java.home")
    assert(File(javaHome).isDirectory)
    return JavaSdk.getInstance().createJdk("Full JDK", javaHome, true)
  }
}
