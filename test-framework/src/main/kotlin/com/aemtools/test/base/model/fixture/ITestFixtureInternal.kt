package com.aemtools.test.base.model.fixture

import com.aemtools.test.base.model.assertion.IAssertionContext

/**
 * @author Dmytro Primshyts
 */
interface ITestFixtureInternal {

  fun init()

  fun test()

  fun assertionContext(): IAssertionContext

}
