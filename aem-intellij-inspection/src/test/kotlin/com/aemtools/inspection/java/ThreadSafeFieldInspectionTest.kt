package com.aemtools.inspection.java

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JdkProjectDescriptor
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import com.intellij.testFramework.LightProjectDescriptor
import java.io.File

/**
 * Test for [ThreadSafeFieldInspection].
 *
 * @author Dmytro Primshyts
 */
class ThreadSafeFieldInspectionTest : BaseLightTest(true) {

  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testThreadSafeFieldInspection() {
    with(myFixture) {
      addClass("""
        package org.apache.sling.api.resource;

        interface ResourceResolver {}
      """)

      addClass("""
        package javax.jcr;

        interface Session {}
      """)

      addClass("""
        package javax.servlet;

        interface Servlet {}
      """)
    }

    myFixture.enableInspections(ThreadSafeFieldInspection())

    myFixture.testInspection("com/aemtools/inspection/java/thread-safe-field",
        LocalInspectionToolWrapper(ThreadSafeFieldInspection()))
  }


  override fun getProjectDescriptor(): LightProjectDescriptor {
    return JdkProjectDescriptor()
  }

}
