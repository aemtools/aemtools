package com.aemtools.index

import com.aemtools.index.dataexternalizer.TemplateDefinitionExternalizer
import com.aemtools.index.indexer.HtlTemplateIndexer
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.lang.htl.file.HtlFileType
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * Collects Htl files containing *data-sly-template* for quick search.
 *
 * @author Dmytro_Troynikov
 */
class HtlTemplateIndex : XmlIndex<TemplateDefinition>() {

  companion object {
    val HTL_TEMPLATE_ID: ID<String, TemplateDefinition>
        = ID.create<String, TemplateDefinition>("HtlTemplateIndex")

    /**
     * Rebuild Htl template index.
     */
    fun rebuildIndex() = FileBasedIndex.getInstance()
        .requestRebuild(HTL_TEMPLATE_ID)
  }

  override fun getIndexer(): DataIndexer<String, TemplateDefinition, FileContent>
      = HtlTemplateIndexer

  override fun getInputFilter(): FileBasedIndex.InputFilter
      = FileBasedIndex.InputFilter {
    it.fileType == HtlFileType
  }

  override fun getValueExternalizer(): DataExternalizer<TemplateDefinition>
      = TemplateDefinitionExternalizer

  override fun getName(): ID<String, TemplateDefinition>
      = HTL_TEMPLATE_ID

}
