package com.aemtools.completion.html.inserthandler

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for HTL text insert handler [HtlTextInsertHandler] for attribute values in HTL v 1.4.
 *
 * @author Kostiantyn Diachenko
 */
class HtlTextInsertHandlerV14Test : HtlTextInsertHandlerTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }

  fun testHtlAttributesInsertExpressionDataSlySet() = checkAutoCompletion()
}
