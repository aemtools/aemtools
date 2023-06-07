package com.aemtools.codeinsight.xml.schema

import com.aemtools.codeinsight.xml.ns.AemNamespaces
import com.intellij.javaee.ResourceRegistrar
import com.intellij.javaee.StandardResourceProvider

class AemFileVaultSchemaResourceProvider : StandardResourceProvider {
  override fun registerResources(registrar: ResourceRegistrar) {
    AemNamespaces.values().forEach { registrar.addIgnoredResource(it.namespace) }
  }
}
