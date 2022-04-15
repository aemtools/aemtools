package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.test.typedescriptor.typeDescriptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.*


/**
 * @author Dmytro Primshyts
 */
class TypeDescriptorUtilKtTest {

    @Test
    fun `mayBeIteratedUpon for array`() {
      val descriptor = typeDescriptor {
        array = true
      }

      assertThat(descriptor.mayBeIteratedUpon())
          .isTrue()
    }

  @Test
  fun `mayBeIteratedUpon for iterable`() {
    val descriptor = typeDescriptor {
      iterable = true
    }

    assertThat(descriptor.mayBeIteratedUpon())
        .isTrue()
  }

  @Test
  fun `mayBeIteratedUpon for map`() {
    val descriptor = typeDescriptor {
      map = true
    }

    assertThat(descriptor.mayBeIteratedUpon())
        .isTrue()
  }

  @Test
  fun `mayBeIteratedUpon for default`() {
    assertThat(typeDescriptor {  }.mayBeIteratedUpon())
        .isFalse()
  }

}
