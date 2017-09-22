package com.aemtools.blocks.base.model.fixture

import com.aemtools.blocks.base.model.assertion.IAssertionContext

/**
 * @author Dmytro Troynikov
 */
interface ITestFixtureInternal {

  fun init()

  fun test()

  fun assertionContext(): IAssertionContext

}
