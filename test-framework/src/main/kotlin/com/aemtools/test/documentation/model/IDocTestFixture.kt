package com.aemtools.test.documentation.model

import com.aemtools.test.base.model.fixture.ITestFixture
import org.intellij.lang.annotations.Language

/**
 * @author Dmytro Troynikov
 */
interface IDocTestFixture : ITestFixture {

  /**
   * Specify text of required documentation.
   *
   * @param result html text of required documentation
   */
  fun documentation(@Language("HTML") result: String)

}
