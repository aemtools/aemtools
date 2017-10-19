package com.aemtools.index

import com.aemtools.index.dataexternalizer.LocalizationModelExternalizer
import com.aemtools.index.indexer.LocalizationModelIndexer
import com.aemtools.index.model.LocalizationModel
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * @author Dmytro Troynikov
 */
class LocalizationIndex : XmlIndex<LocalizationModel>() {
  companion object {
    val LOCALIZATION_INDEX: ID<String, LocalizationModel>
        = ID.create<String, LocalizationModel>("LocalizationIndex")
  }

  override fun getValueExternalizer(): DataExternalizer<LocalizationModel>
      = LocalizationModelExternalizer

  override fun getName(): ID<String, LocalizationModel>
      = LOCALIZATION_INDEX

  override fun getIndexer(): DataIndexer<String, LocalizationModel, FileContent>
      = LocalizationModelIndexer

  override fun getInputFilter(): FileBasedIndex.InputFilter
      = FileBasedIndex.InputFilter {
    it.name.endsWith(".xml")
  }

}
