package com.aemtools.test.base.model.file

/**
 * @author Dmytro Primshyts
 */
interface IFileFixtureDescriptor {

  /**
   * Check if current file contains [com.intellij.testFramework.fixtures.CodeInsightTestFixture.CARET_MARKER]
   */
  fun containsCaret(): Boolean

  /**
   * Add current file fixture to actual fixture.
   */
  fun initialize()

}
