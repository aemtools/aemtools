package com.aemtools.codeinsight.xml.schema

import com.aemtools.codeinsight.xml.ns.AemNamespaces
import com.aemtools.common.constant.const.file_names.CQ_DIALOG_XML
import com.aemtools.common.constant.const.file_names.CQ_EDITCONFIG_XML
import com.aemtools.common.constant.const.file_names.DIALOG_XML
import com.aemtools.common.constant.const.file_names.FILE_VAULT_FILE_NAME
import com.intellij.openapi.module.Module
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile
import com.intellij.xml.XmlSchemaProvider

class AemFileVaultSchemaProvider : XmlSchemaProvider() {

  override fun getSchema(url: String, module: Module?, baseFile: PsiFile): XmlFile? {
    return null
  }

  override fun isAvailable(file: XmlFile): Boolean {
    val fileName = file.name
    return SUPPORTED_FILE_NAMES.contains(fileName)
  }

  override fun getAvailableNamespaces(file: XmlFile, tagName: String?): MutableSet<String> {
    return AemNamespaces.values().map { it.namespace }.toMutableSet()
  }

  override fun getDefaultPrefix(namespace: String, context: XmlFile): String? {
    return AemNamespaces.values().find { it.namespace == namespace }?.prefix
  }

  companion object {
    val SUPPORTED_FILE_NAMES = listOf(CQ_DIALOG_XML, CQ_EDITCONFIG_XML, FILE_VAULT_FILE_NAME, DIALOG_XML)
  }
}
