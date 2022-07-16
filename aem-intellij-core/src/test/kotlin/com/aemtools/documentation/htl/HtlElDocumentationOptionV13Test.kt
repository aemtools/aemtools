package com.aemtools.documentation.htl

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for HTL options documentation for HTL v 1.3.
 *
 * @author Kostiantyn Diachenko
 */
class HtlElDocumentationOptionV13Test : HtlElDocumentationOptionTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_3)
  }
}
