package com.aemtools.test.action

import com.aemtools.test.base.BasePlatformLightTest

/**
 * @author Dmytro Troynikov
 */
interface TypeActionTest {

  /**
   * One file based type action test.
   *
   * @param fileName name of input file
   * @param before "before" state of file
   * @param type the string to type
   * @param result "after" state of file
   * @see BasePlatformLightTest,
   */
  fun BasePlatformLightTest.typedActionTest(fileName: String,
                                            before: String,
                                            type: String,
                                            result: String) {
    fixture().apply {
      configureByText(fileName, before)
      type(type)
      checkResult(result)
    }
  }

}
