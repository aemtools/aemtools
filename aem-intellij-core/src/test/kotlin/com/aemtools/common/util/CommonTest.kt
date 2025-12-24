package com.aemtools.common.util

import com.aemtools.common.constant.Const
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author Dmytro Primshyts
 */
class CommonTest {

  @Test
  fun `isHtlAttributeName should match declaration by attribute name`() {
    Const.Htl.DECLARATION_ATTRIBUTES.forEach {
      assertThat(it.isHtlAttributeName())
          .isTrue()
    }
  }

  @Test
  fun `isHtlAttributeName should match declaration attribute by name + var name`() {
    Const.Htl.DECLARATION_ATTRIBUTES.forEach {
      assertThat("$it.name".isHtlAttributeName())
          .isTrue()
    }
  }

  @Test
  fun `isHtlAttributeName should match single attributes`() {
    Const.Htl.SINGLE_ATTRIBUTES.forEach {
      assertThat(it.isHtlAttributeName())
          .isTrue()
    }
  }

  @Test
  fun `isHtlAttributeName should not match single attribute with variable`() {
    Const.Htl.SINGLE_ATTRIBUTES.forEach {
      assertThat("$it.name".isHtlAttributeName())
          .isFalse()
    }
  }

}
