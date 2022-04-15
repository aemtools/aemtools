package com.aemtools.lang.htl.psi

import com.aemtools.test.base.BaseLightTest

/**
 * @author Dmytro Primshyts
 */
class HtlElementFactoryTest : BaseLightTest(false) {

  fun testDotAccessIdentifier() {
    val accessIdentifier = com.aemtools.lang.htl.psi.HtlElementFactory.createDotAccessIdentifier("identifier", project)

    assertEquals(".identifier", accessIdentifier?.text)
    assertEquals(HtlTypes.DOT_ACCESS, accessIdentifier?.elementType)
  }

  fun testArrayLikeAccessSinglequoted() {
    val accessIdentifier = com.aemtools.lang.htl.psi.HtlElementFactory.createArrayLikeAccessSingleQuoted("identifier", project)

    assertEquals("['identifier']", accessIdentifier?.text)
    assertEquals(HtlTypes.ARRAY_LIKE_ACCESS, accessIdentifier?.elementType)
  }

  fun testArrayLikeAccessDoubleQuoted() {
    val accessIdentifier = com.aemtools.lang.htl.psi.HtlElementFactory.createArrayLikeAccessDoublequoted("identifier", project)

    assertEquals("[\"identifier\"]", accessIdentifier?.text)
    assertEquals(HtlTypes.ARRAY_LIKE_ACCESS, accessIdentifier?.elementType)
  }

  fun testOption() {
    val option = com.aemtools.lang.htl.psi.HtlElementFactory.createOption("name", project)

    assertEquals("name", option?.text)
    assertTrue(HtlVariableName::class.java.isAssignableFrom(option?.javaClass))
  }

}
