package com.aemtools.index

import com.aemtools.index.dataexternalizer.AemComponentClassicDialogDefinitionExternalizer
import com.aemtools.index.indexer.AemComponentClassicDialogIndexer
import com.aemtools.index.model.dialog.AemComponentClassicDialogDefinition
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * Indexes classic dialog files (dialog.xml).
 *
 * @author Dmytro Troynikov
 */
class AemComponentClassicDialogIndex : XmlIndex<AemComponentClassicDialogDefinition>() {

  companion object {
    val AEM_COMPONENT_CLASSIC_DIALOG_INDEX_ID: ID<String, AemComponentClassicDialogDefinition>
        = ID.create<String, AemComponentClassicDialogDefinition>("AemComponentClassicDialogDefinitionIndex")
  }

  override fun getValueExternalizer(): DataExternalizer<AemComponentClassicDialogDefinition>
      = AemComponentClassicDialogDefinitionExternalizer

  override fun getName(): ID<String, AemComponentClassicDialogDefinition>
      = AEM_COMPONENT_CLASSIC_DIALOG_INDEX_ID

  override fun getIndexer(): DataIndexer<String, AemComponentClassicDialogDefinition, FileContent>
      = AemComponentClassicDialogIndexer

  override fun getInputFilter(): FileBasedIndex.InputFilter
      = FileBasedIndex.InputFilter {
    it.name == "dialog.xml"
  }

}

