package com.aemtools.index

import com.aemtools.index.dataexternalizer.OSGiConfigurationExternalizer
import com.aemtools.index.indexer.JsonOSGiConfigIndexer
import com.aemtools.index.model.OSGiConfigurationIndexModel
import com.intellij.json.JsonFileType
import com.intellij.util.indexing.*
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor

class JsonOSGiConfigIndex: FileBasedIndexExtension<String, OSGiConfigurationIndexModel>() {

  companion object {
    val JSON_OSGI_INDEX_ID: ID<String, OSGiConfigurationIndexModel>
        = ID.create("JsonOSGiConfigIndex")
  }

  override fun getName(): ID<String, OSGiConfigurationIndexModel> = JSON_OSGI_INDEX_ID

  override fun getIndexer(): DataIndexer<String, OSGiConfigurationIndexModel, FileContent> = JsonOSGiConfigIndexer

  override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE

  override fun getValueExternalizer(): DataExternalizer<OSGiConfigurationIndexModel> = OSGiConfigurationExternalizer

  override fun getVersion(): Int = 1

  override fun getInputFilter(): FileBasedIndex.InputFilter = FileBasedIndex.InputFilter {
    it.fileType == JsonFileType.INSTANCE
        && it.path.contains("config")
        && it.path.endsWith(".cfg.json")
  }

  override fun dependsOnFileContent(): Boolean = true

}
