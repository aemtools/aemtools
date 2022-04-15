package com.aemtools.test.reference

import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JdkProjectDescriptor
import com.aemtools.test.reference.model.IReferenceTestFixture
import com.aemtools.test.reference.model.ReferenceTestFixture
import com.intellij.testFramework.LightProjectDescriptor

/**
 * @author Dmytro Primshyts
 */
abstract class BaseReferenceTest(withAemUberJar: Boolean = false)
  : BaseLightTest(withAemUberJar) {

  fun testReference(unit: IReferenceTestFixture.() -> Unit) {
    val fixture = ReferenceTestFixture(fixture = myFixture).apply { unit() }

    fixture.init()

    fixture.test()
  }

  override fun getProjectDescriptor(): LightProjectDescriptor
      = JdkProjectDescriptor()

}

