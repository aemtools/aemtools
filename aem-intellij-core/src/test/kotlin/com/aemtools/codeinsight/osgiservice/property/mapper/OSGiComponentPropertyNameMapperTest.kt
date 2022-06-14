package com.aemtools.codeinsight.osgiservice.property.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Tests for [OSGiComponentPropertyNameMapper].
 *
 * @author Kostiantyn Diachenko
 */
class OSGiComponentPropertyNameMapperTest {

  @ParameterizedTest
  @CsvSource(value = [
    "myProperty143:myProperty143",
    "\$new:new",
    "my\$\$prop:my\$prop",
    "dot_prop:dot.prop",
    "_secret:.secret",
    "another__prop:another_prop",
    "three___prop:three_.prop",
    "four_\$__prop:four._prop",
    "five_\$_prop:five..prop",
    "six\$_\$prop:six-prop",
    "seven\$\$_\$prop:seven\$.prop",
    "eight\$_\$prop_prefix:eight-prop.prefix"
  ], delimiter = ':')
  fun testMappingOsgiConfigMethodNameToPropertyName(input: String, expected: String) {
    assertEquals(expected, OSGiComponentPropertyNameMapper.mapByMethodName(input))
  }

}
