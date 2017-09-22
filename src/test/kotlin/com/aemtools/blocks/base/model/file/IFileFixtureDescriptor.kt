package com.aemtools.blocks.base.model.file

/**
 * @author Dmytro Troynikov
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
