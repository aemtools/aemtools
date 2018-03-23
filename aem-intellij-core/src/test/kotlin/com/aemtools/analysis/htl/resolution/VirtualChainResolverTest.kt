package com.aemtools.analysis.htl.resolution

import com.aemtools.analysis.htl.callchain.elements.virtual.BaseVirtualCallChainElement
import com.aemtools.test.typedescriptor.typeDescriptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Test for [VirtualChainResolver].
 *
 * @author Dmytro Troynikov
 */
class VirtualChainResolverTest {

  @Test
  fun `should not consider starting type`() {
    val inputType = typeDescriptor {
      array = true
      map = true
      iterable = true
      name = "bean"
      subtype {
        nameInParent = "subtype"
      }
    }

    assertThat(VirtualChainResolver.nestedIterables(BaseVirtualCallChainElement(
        "bean",
        inputType
    )))
        .isEmpty()
  }

  @Test
  fun `nestedIterables should resolve array`() {
    val inputType = typeDescriptor {
      name = "bean"
      subtype {
        array = true
        nameInParent = "subtype"
      }
    }

    val result = VirtualChainResolver.nestedIterables(BaseVirtualCallChainElement(
        "bean",
        inputType
    ))

    assertThat(result)
        .hasSize(1)

    val item = result.first()

    assertThat(item.lookupString)
        .isEqualTo("bean.subtype")
  }

  @Test
  fun `nestedIterables should resolve map`() {
    val inputType = typeDescriptor {
      name = "bean"
      subtype {
        map = true
        nameInParent = "subtype"
      }
    }

    val result = VirtualChainResolver.nestedIterables(BaseVirtualCallChainElement(
        "bean",
        inputType
    ))

    assertThat(result)
        .hasSize(1)

    assertThat(result.first().lookupString)
        .isEqualTo("bean.subtype")
  }

  @Test
  fun `nestedIterables should resolve iterable`() {
    val inputType = typeDescriptor {
      name = "bean"
      subtype {
        iterable = true
        nameInParent = "subtype"
      }
    }

    val result = VirtualChainResolver.nestedIterables(BaseVirtualCallChainElement(
        "bean",
        inputType
    ))

    assertThat(result)
        .hasSize(1)

    assertThat(result.first().lookupString)
        .isEqualTo("bean.subtype")
  }

  @Test
  fun `nestedIterables complex`() {
    val inputType = typeDescriptor {
      name = "bean"
      subtype {
        nameInParent = "subtype1"
        subtype {
          array = true
          nameInParent = "array"
        }

        subtype {
          iterable = true
          nameInParent = "iterable"
        }

        subtype {
          map = true
          nameInParent = "map"
        }
      }

      subtype {
        nameInParent = "subtype2"
        subtype {
          array = true
          nameInParent = "array"
        }

        subtype {
          iterable = true
          nameInParent = "iterable"
        }

        subtype {
          map = true
          nameInParent = "map"
        }
      }

      subtype {
        nameInParent = "subtype3"
        subtype {
          nameInParent = "nothingToDoHere"
        }
      }
    }

    val result = VirtualChainResolver.nestedIterables(BaseVirtualCallChainElement(
        "bean",
        inputType
    ))

    assertThat(result.map { it.lookupString })
        .contains(
            "bean.subtype1.array",
            "bean.subtype1.iterable",
            "bean.subtype1.map",
            "bean.subtype2.array",
            "bean.subtype2.iterable",
            "bean.subtype2.map"
        )
  }

  @Test
  fun `nestedIterables intersections`() {
    val inputType = typeDescriptor {
      name = "bean"
      subtype {
        array = true
        nameInParent = "array"
        subtype {
          map = true
          nameInParent = "map"
          subtype {
            iterable = true
            nameInParent = "iterable"
          }
        }
      }
    }

    val result = VirtualChainResolver.nestedIterables(BaseVirtualCallChainElement(
        "bean",
        inputType
    ))

    assertThat(result.map { it.lookupString })
        .contains(
            "bean.array",
            "bean.array.map",
            "bean.array.map.iterable"
        )
  }

}
