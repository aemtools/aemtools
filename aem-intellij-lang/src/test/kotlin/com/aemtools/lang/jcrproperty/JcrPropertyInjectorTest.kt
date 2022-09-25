package com.aemtools.lang.jcrproperty

import com.aemtools.test.junit.MockitoExtension
import com.aemtools.test.util.mock
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

/**
 * Test for [JcrPropertyInjector].
 *
 * @author Dmytro Primshyts
 */
@ExtendWith(MockitoExtension::class)
class JcrPropertyInjectorTest {

  private val tested = JcrPropertyInjector()

  @Mock
  lateinit var xmlAttributeValue: XmlAttributeValueImpl

  @Mock
  lateinit var xmlAttribute: XmlAttribute

  @Mock
  lateinit var xmlTag: XmlTag

  @Mock
  lateinit var psiFile: PsiFile

  @Mock
  lateinit var registrar: MultiHostRegistrar

  @Before
  fun init() {
    `when`(xmlAttributeValue.parent)
        .thenReturn(xmlAttribute)

    `when`(xmlAttribute.parent)
        .thenReturn(xmlTag)

    mockContainingFile(".content.xml")

    mockXmlAttributeValue("test")
  }

  @Test
  fun `should not work for non XmlAttributeValue input`() {
    val tag = mock<XmlTag>()

    tested.getLanguagesToInject(registrar, tag)

    verify(tag, never()).parent
  }

  @Test
  fun `should not work for non XmlAttribute child`() {
    `when`(xmlAttributeValue.parent).thenReturn(null)

    tested.getLanguagesToInject(registrar, xmlAttributeValue)

    verify(xmlAttributeValue, never())
        .containingFile
  }

  @Test
  fun `should inject into ClientLibraryFolder embed`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "cq:ClientLibraryFolder"
    attributeName = "embed"
    attributeValue = "[cat1,cat2]"
    expectedTextRange = TextRange.create(1, 10)
  }

  @Test
  fun `should inject into ClientLibraryFolder categories`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "cq:ClientLibraryFolder"
    attributeName = "categories"
    attributeValue = "[cat1,cat2]"
    expectedTextRange = TextRange.create(1, 10)
  }

  @Test
  fun `should inject into ClientLibraryFolder channels`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "cq:ClientLibraryFolder"
    attributeName = "dependencies"
    attributeValue = "mobile"
    expectedTextRange = TextRange.create(1, 5)
  }

  @Test
  fun `should inject into ClientLibraryFolder dependencies`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "cq:ClientLibraryFolder"
    attributeName = "dependencies"
    attributeValue = "[cat1,cat2]"
    expectedTextRange = TextRange.create(1, 10)
  }

  @Test
  fun `should inject into OSGi config`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "sling:OsgiConfig"
    attributeName = "some.key"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into OSGi config with empty value`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "sling:OsgiConfig"
    attributeName = "some.key"
    attributeValue = ""
    expectedTextRange = TextRange.create(0, 0)
  }

  @Test
  fun `should inject into cqComponent componentGroup`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "cq:Component"
    attributeName = "componentGroup"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cqComponent sling resourceSuperType`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "cq:Component"
    attributeName = "sling:resourceSuperType"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cqComponent cq isContainer`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "cq:Component"
    attributeName = "cq:isContainer"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cqComponent cq noDecoration`() = testAttribute {
    fileName = ".content.xml"
    primaryType = "cq:Component"
    attributeName = "cq:noDecoration"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cq EditConfig jcr_primaryType`() = testAttribute {
    fileName = "_cq_editConfig.xml"
    primaryType = "cq:EditConfig"
    attributeName = "jcr:primaryType"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cq EditConfig cq_actions`() = testAttribute {
    fileName = "_cq_editConfig.xml"
    primaryType = "cq:EditConfig"
    attributeName = "cq:actions"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cq EditConfig cq_layout`() = testAttribute {
    fileName = "_cq_editConfig.xml"
    primaryType = "cq:EditConfig"
    attributeName = "cq:layout"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cq EditConfig cq_dialogMode`() = testAttribute {
    fileName = "_cq_editConfig.xml"
    primaryType = "cq:EditConfig"
    attributeName = "cq:dialogMode"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cq EditConfig cq_emptyText`() = testAttribute {
    fileName = "_cq_editConfig.xml"
    primaryType = "cq:EditConfig"
    attributeName = "cq:emptyText"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into cq EditConfig cq_inherit`() = testAttribute {
    fileName = "_cq_editConfig.xml"
    primaryType = "cq:EditConfig"
    attributeName = "cq:inherit"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into rep_policy jcr_primaryType`() = testAttribute {
    fileName = "_rep_policy.xml"
    primaryType = ""
    attributeName = "jcr:primaryType"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into rep_policy rep_principalName`() = testAttribute {
    fileName = "_rep_policy.xml"
    primaryType = ""
    attributeName = "rep:principalName"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  @Test
  fun `should inject into rep_policy rep_privileges`() = testAttribute {
    fileName = "_rep_policy.xml"
    primaryType = ""
    attributeName = "rep:privileges"
    attributeValue = "value"
    expectedTextRange = TextRange.create(1, 4)
  }

  private fun testAttribute(jcrPropertyInjectorTestParams: JcrPropertyInjectorTestParams.() -> Unit) {
    val params = JcrPropertyInjectorTestParams().apply(jcrPropertyInjectorTestParams)
    mockXmlAttributeName(params.attributeName!!)
    mockXmlAttributeValue(params.attributeValue!!)
    mockContainingFile(params.fileName!!)

    val mockedPrimaryType = createXmlAttribute("jcr:primaryType", params.primaryType!!)
    mockXmlTagAttributes(arrayOf(mockedPrimaryType))

    tested.getLanguagesToInject(registrar, xmlAttributeValue)

    verify(registrar).startInjecting(JcrPropertyLanguage)
    verify(registrar)
        .addPlace(
            null,
            null,
            xmlAttributeValue,
            params.expectedTextRange!!
        )
    verify(registrar).doneInjecting()
  }

  private fun mockXmlTagAttributes(mockClientLibAttributes: Array<XmlAttribute>) {
    `when`(xmlTag.attributes).thenReturn(mockClientLibAttributes)
  }

  private fun mockXmlAttributeValue(value: String) {
    `when`(xmlAttributeValue.text).thenReturn(value)
  }

  private fun createXmlAttribute(name: String, value: String): XmlAttribute {
    val mockXmlAttribute = mock<XmlAttribute>()
    `when`(mockXmlAttribute.name).thenReturn(name)
    `when`(mockXmlAttribute.value).thenReturn(value)
    return mockXmlAttribute
  }

  private fun mockContainingFile(fileName: String) {
    `when`(xmlAttributeValue.containingFile)
        .thenReturn(psiFile)
    `when`(psiFile.name)
        .thenReturn(fileName)
  }

  private fun mockXmlAttributeName(name: String) {
    `when`(xmlAttribute.name)
        .thenReturn(name)
    `when`(xmlAttributeValue.parent)
        .thenReturn(xmlAttribute)
    `when`(xmlAttribute.parent)
        .thenReturn(xmlTag)
  }

  @Test
  fun `elementsToInjectIn should return XmlAttributeValue`() {
    val result = tested.elementsToInjectIn()

    assertThat(result)
        .contains(XmlAttributeValue::class.java)
  }

  class JcrPropertyInjectorTestParams {
    var fileName: String? = null
    var primaryType: String? = null
    var attributeName: String? = null
    var attributeValue: String? = null

    var expectedTextRange: TextRange? = null
  }

}
