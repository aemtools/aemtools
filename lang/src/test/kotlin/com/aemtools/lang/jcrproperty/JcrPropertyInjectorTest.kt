package com.aemtools.lang.jcrproperty

import com.aemtools.test.junit.MockitoExtension
import com.aemtools.test.util.mock
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

/**
 * Test for [JcrPropertyInjector].
 *
 * @author Dmytro Primshyts
 */
@ExtendWith(MockitoExtension::class)
class JcrPropertyInjectorTest {

  val tested = JcrPropertyInjector()

  @Mock
  lateinit var xmlAttributeValue: XmlAttributeValueImpl

  @Mock
  lateinit var xmlAttribute: XmlAttribute

  @Mock
  lateinit var psiFile: PsiFile

  @Mock
  lateinit var registrar: MultiHostRegistrar

  @Before
  fun init() {
    `when`(xmlAttributeValue.parent)
        .thenReturn(xmlAttribute)

    `when`(xmlAttributeValue.containingFile)
        .thenReturn(psiFile)
    `when`(psiFile.name)
        .thenReturn(".content.xml")

    `when`(xmlAttributeValue.text)
        .thenReturn("test")
  }

  @Test
  fun `should not work for non XmlAttributeValue input`() {
    val tag = mock<XmlTag>()

    tested.getLanguagesToInject(registrar, tag)

    verify(tag, never())
        .parent
  }

  @Test
  fun `should not work for non XmlAttribute child`() {
    `when`(xmlAttributeValue.parent)
        .thenReturn(null)

    tested.getLanguagesToInject(registrar, xmlAttributeValue)

    verify(xmlAttributeValue, never())
        .containingFile
  }

  @Test
  fun `should inject into embed`() = testAttribute("embed")

  @Test
  fun `should inject into categories`() = testAttribute("categories")

  @Test
  fun `should inject into channels`() = testAttribute("channels")

  @Test
  fun `should inject into dependencies`() = testAttribute("dependencies")

  fun testAttribute(name: String) {
    `when`(xmlAttribute.name)
        .thenReturn(name)
    `when`(xmlAttributeValue.parent)
        .thenReturn(xmlAttribute)
    `when`(xmlAttributeValue.containingFile)
        .thenReturn(psiFile)
    `when`(psiFile.name)
        .thenReturn(".content.xml")
    `when`(xmlAttributeValue.text)
        .thenReturn("test")

    tested.getLanguagesToInject(registrar, xmlAttributeValue)

    verify(registrar)
        .startInjecting(JcrPropertyLanguage)
    verify(registrar)
        .addPlace(
            null,
            null,
            xmlAttributeValue,
            TextRange.create(1, 3)
        )
    verify(registrar)
        .doneInjecting()
  }

  @Test
  fun `elementsToInjectIn should return XmlAttributeValue`() {
    val result = tested.elementsToInjectIn()

    assertThat(result)
        .contains(XmlAttributeValue::class.java)
  }

}
