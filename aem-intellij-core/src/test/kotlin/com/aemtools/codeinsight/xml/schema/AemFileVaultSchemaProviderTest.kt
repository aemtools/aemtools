package com.aemtools.codeinsight.xml.schema

import com.aemtools.test.junit.MockitoExtension
import com.intellij.psi.xml.XmlFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito

@ExtendWith(MockitoExtension::class)
class AemFileVaultSchemaProviderTest {

  @Mock
  lateinit var xmlFile: XmlFile

  @ParameterizedTest
  @CsvSource(
      ".content.xml,true",
      "_cq_dialog.xml,true",
      "_cq_editConfig.xml,true",
      "dialog.xml,true",
      "test.xml,false",
      "_rep_policy.xml,false"
  )
  fun testIsAvailable(fileName: String, isAvailable: Boolean) {
    Mockito.`when`(xmlFile.name).thenReturn(fileName)
    val provider = AemFileVaultSchemaProvider()

    assertEquals(isAvailable, provider.isAvailable(xmlFile))
  }

  @Test
  fun testGetAvailableNamespaces() {
    val provider = AemFileVaultSchemaProvider()

    val availableNamespaces = provider.getAvailableNamespaces(xmlFile, "")
    assertEquals(8, availableNamespaces.size)
    assertThat(availableNamespaces)
        .containsExactly(
            "http://www.day.com/jcr/cq/1.0",
            "http://www.jcp.org/jcr/1.0",
            "http://www.day.com/crx/1.0",
            "http://www.adobe.com/jcr/granite/1.0",
            "http://www.jcp.org/jcr/nt/1.0",
            "http://jackrabbit.apache.org/oak/ns/1.0",
            "http://sling.apache.org/jcr/sling/1.0",
            "http://www.jcp.org/jcr/mix/1.0"
        )
  }

  @ParameterizedTest
  @CsvSource(
      "cq,http://www.day.com/jcr/cq/1.0",
      "jcr,http://www.jcp.org/jcr/1.0",
      "crx,http://www.day.com/crx/1.0",
      "granite,http://www.adobe.com/jcr/granite/1.0",
      "nt,http://www.jcp.org/jcr/nt/1.0",
      "oak,http://jackrabbit.apache.org/oak/ns/1.0",
      "sling,http://sling.apache.org/jcr/sling/1.0",
      "mix,http://www.jcp.org/jcr/mix/1.0",
      ",otherNs"
  )
  fun testGetDefaultNamespace(prefix: String?, namespace: String) {
    val provider = AemFileVaultSchemaProvider()

    assertEquals(prefix, provider.getDefaultPrefix(namespace, xmlFile))
  }

  @Test
  fun testGetSchema() {
    val provider = AemFileVaultSchemaProvider()

    assertNull(provider.getSchema("", null, xmlFile))
  }
}
