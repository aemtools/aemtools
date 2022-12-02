package com.aemtools.test.sdk

import com.intellij.openapi.projectRoots.Sdk
import com.intellij.testFramework.IdeaTestUtil
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

/**
 * @author Kostiantyn Diachenko
 */
class TestSdk {

  companion object {
    private val TEST_SDK_JAVA_11: Sdk = IdeaTestUtil.createMockJdk(
        "mockedTestJava11",
        Paths.get(System.getProperty("test.java.dir")).absolutePathString())

    @JvmStatic
    fun getSdk() = TEST_SDK_JAVA_11
  }
}
