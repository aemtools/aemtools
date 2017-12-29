package com.aemtools.index

import com.aemtools.index.dataexternalizer.AemComponentTouchUIDialogDefinitionExternalizer
import com.aemtools.index.indexer.AemComponentTouchUIDialogIndexer
import com.aemtools.index.model.dialog.AemComponentTouchUIDialogDefinition
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * Indexes touch ui dialogs (_cq_dialog/.content.xml).
 *
 * @author Dmytro Troynikov
 */
class AemComponentTouchUIDialogIndex : XmlIndex<AemComponentTouchUIDialogDefinition>() {
  companion object {
    val AEM_COMPONENT_TOUCH_UI_DIALOG_INDEX: ID<String, AemComponentTouchUIDialogDefinition>
        = ID.create<String, AemComponentTouchUIDialogDefinition>("AemComponentTouchUIDialogIndex")
  }

  override fun getValueExternalizer(): DataExternalizer<AemComponentTouchUIDialogDefinition>
      = AemComponentTouchUIDialogDefinitionExternalizer

  override fun getName(): ID<String, AemComponentTouchUIDialogDefinition>
      = AEM_COMPONENT_TOUCH_UI_DIALOG_INDEX

  override fun getIndexer(): DataIndexer<String, AemComponentTouchUIDialogDefinition, FileContent>
      = AemComponentTouchUIDialogIndexer

  override fun getInputFilter(): FileBasedIndex.InputFilter
      = FileBasedIndex.InputFilter {
    it.path.endsWith("_cq_dialog/.content.xml")
  }
}
