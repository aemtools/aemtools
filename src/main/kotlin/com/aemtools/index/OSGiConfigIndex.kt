package com.aemtools.index

import com.aemtools.index.dataexternalizer.OSGiConfigurationExternalizer
import com.aemtools.index.indexer.OSGiConfigIndexer
import com.aemtools.index.model.OSGiConfiguration
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * Indexes OSGi configuration files.
 *
 * @author Dmytro_Troynikov
 */
class OSGiConfigIndex : XmlIndex<OSGiConfiguration>() {

  companion object {
    val OSGI_INDEX_ID: ID<String, OSGiConfiguration>
        = ID.create<String, OSGiConfiguration>("OSGiConfigIndex")
  }

  override fun getIndexer(): DataIndexer<String, OSGiConfiguration, FileContent>
      = OSGiConfigIndexer

  override fun getInputFilter(): FileBasedIndex.InputFilter
      = FileBasedIndex.InputFilter {
    it.fileType == XmlFileType.INSTANCE && it.path.contains("config")
  }

  override fun getValueExternalizer(): DataExternalizer<OSGiConfiguration>
      = OSGiConfigurationExternalizer

  override fun getName(): ID<String, OSGiConfiguration>
      = OSGI_INDEX_ID

}
