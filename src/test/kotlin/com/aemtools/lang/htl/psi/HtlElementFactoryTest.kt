package com.aemtools.lang.htl.psi

import com.aemtools.blocks.base.BaseLightTest

/**
 * @author Dmytro Troynikov
 */
class HtlElementFactoryTest : BaseLightTest(false) {

  fun testDotAccessIdentifier() {
    val accessIdentifier = HtlElementFactory.createDotAccessIdentifier("identifier", project)

    assertEquals(".identifier", accessIdentifier?.text)
    assertEquals(HtlTypes.DOT_ACCESS, accessIdentifier?.elementType)
  }

  fun testArrayLikeAccessSinglequoted() {
    val accessIdentifier = HtlElementFactory.createArrayLikeAccessSingleQuoted("identifier", project)

    assertEquals("['identifier']", accessIdentifier?.text)
    assertEquals(HtlTypes.ARRAY_LIKE_ACCESS, accessIdentifier?.elementType)
  }

  fun testArrayLikeAccessDoubleQuoted() {
    val accessIdentifier = HtlElementFactory.createArrayLikeAccessDoublequoted("identifier", project)

    assertEquals("[\"identifier\"]", accessIdentifier?.text)
    assertEquals(HtlTypes.ARRAY_LIKE_ACCESS, accessIdentifier?.elementType)
  }

  fun testOption() {
    val option = HtlElementFactory.createOption("name", project)

    assertEquals("name", option?.text)
    assertTrue(HtlVariableName::class.java.isAssignableFrom(option?.javaClass))
  }

}
