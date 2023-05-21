package com.aemtools.codeinsight.xml.schema

import com.intellij.javaee.ResourceRegistrarImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AemFileVaultSchemaResourceProviderTest {
  @Test
  fun testRegisteringAemFIleVaultResourcesAsIgnored() {
    val provider = AemFileVaultSchemaResourceProvider()
    val resourceRegistrar = ResourceRegistrarImpl()

    provider.registerResources(resourceRegistrar)

    assertThat(resourceRegistrar.ignored)
        .hasSize(8)
        .contains(
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
}
