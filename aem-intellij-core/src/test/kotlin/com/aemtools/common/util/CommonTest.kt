package com.aemtools.common.util

import com.aemtools.common.constant.const
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author Dmytro Primshyts
 */
class CommonTest {

  @Test
  fun `isHtlAttributeName should match declaration by attribute name`() {
    const.htl.DECLARATION_ATTRIBUTES.forEach {
      assertThat(it.isHtlAttributeName())
          .isTrue()
    }
  }

  @Test
  fun `isHtlAttributeName should match declaration attribute by name + var name`() {
    const.htl.DECLARATION_ATTRIBUTES.forEach {
      assertThat("$it.name".isHtlAttributeName())
          .isTrue()
    }
  }

  @Test
  fun `isHtlAttributeName should match single attributes`() {
    const.htl.SINGLE_ATTRIBUTES.forEach {
      assertThat(it.isHtlAttributeName())
          .isTrue()
    }
  }

  @Test
  fun `isHtlAttributeName should not match single attribute with variable`() {
    const.htl.SINGLE_ATTRIBUTES.forEach {
      assertThat("$it.name".isHtlAttributeName())
          .isFalse()
    }
  }

}
