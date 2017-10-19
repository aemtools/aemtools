package com.aemtools.blocks.reference

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.fixture.JdkProjectDescriptor
import com.aemtools.blocks.reference.model.IReferenceTestFixture
import com.aemtools.blocks.reference.model.ReferenceTestFixture
import com.intellij.testFramework.LightProjectDescriptor

/**
 * @author Dmytro Troynikov
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

