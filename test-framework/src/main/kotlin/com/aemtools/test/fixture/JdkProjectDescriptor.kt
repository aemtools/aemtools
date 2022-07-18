package com.aemtools.test.fixture

import com.aemtools.test.sdk.TestSdk
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.testFramework.LightProjectDescriptor

/**
 * @author Dmytro Primshyts
 */
class JdkProjectDescriptor : LightProjectDescriptor() {
  override fun getSdk(): Sdk = TestSdk.getSdk()
}
