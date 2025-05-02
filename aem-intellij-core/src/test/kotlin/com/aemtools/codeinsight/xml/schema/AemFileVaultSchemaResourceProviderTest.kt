package com.aemtools.codeinsight.xml.schema

import com.intellij.javaee.ResourceRegistrar
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AemFileVaultSchemaResourceProviderTest {
  @Test
  fun testRegisteringAemFIleVaultResourcesAsIgnored() {
    val provider = AemFileVaultSchemaResourceProvider()
    val resourceRegistrar = ResourceRegistrarStub()

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

  class ResourceRegistrarStub : ResourceRegistrar {
    val ignored = ArrayList<String>()

    override fun addIgnoredResource(url: String) {
      ignored.add(url)
    }

    override fun addStdResource(resource: String, fileName: String, classLoader: ClassLoader) {
    }

    override fun addStdResource(resource: String, fileName: String, klass: Class<*>?) {
    }

    override fun addStdResource(resource: String, version: String?, fileName: String, aClass: Class<*>?) {
    }

  }
}
