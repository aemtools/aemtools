package com.aemtools.blocks.reference.model

import com.aemtools.blocks.base.model.fixture.ITestFixture
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
interface IReferenceTestFixture : ITestFixture {

  /**
   * Assert that the element under caret should be resolved to item of specified type.
   * @param type the type of resolution item
   * @param strict for _false_ [Class.isAssignableFrom] will be called, otherwise the classes will
   * be checked for equality
   */
  fun shouldResolveTo(type: Class<out PsiElement>, strict: Boolean = false)

  /**
   * Assert that resolved item should contained specified text.
   * @param text the text
   */
  fun shouldContainText(text: String)

  /**
   * Initialize the fixture.
   */
  fun init()

  /**
   * Perform the test.
   */
  fun test()

}
