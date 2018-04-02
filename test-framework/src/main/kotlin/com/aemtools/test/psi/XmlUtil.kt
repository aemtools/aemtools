package com.aemtools.test.psi

import com.aemtools.test.util.mock
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import org.mockito.Mockito.`when`

/**
 * Create "mock" xml attribute with given name and value.
 *
 * @param name the name of attribute
 * @param value the value of attribute
 * @return mocked xml attribute
 */
fun createMockAttribute(name: String, value: String = ""): XmlAttribute {
  val result = mock<XmlAttribute>()

  `when`(result.name).thenReturn(name)

  `when`(result.value).thenReturn(value)

  return result
}

/**
 * Add mocked [XmlAttribute] to current [XmlTag].
 *
 * @param name the name of attribute
 * @param value the value of attribute
 *
 * @receiver [XmlTag]
 * @return mocked xml attribute
 */
fun XmlTag.mockAttribute(name: String, value: String = ""): XmlAttribute {
  val attribute = createMockAttribute(name, value)

  val attributesConcat = if (attributes != null) {
    attributes + attribute
  } else {
    arrayOf(attribute)
  }

  `when`(this.attributes).thenReturn(
      attributesConcat
  )

  `when`(this.getAttribute(name)).thenReturn(attribute)

  return attribute
}
