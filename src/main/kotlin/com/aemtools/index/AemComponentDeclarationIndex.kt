package com.aemtools.index

import com.aemtools.index.dataexternalizer.AemComponentDeclarationExternalizer
import com.aemtools.index.indexer.AemComponentDeclarationIndexer
import com.aemtools.index.model.AemComponentDefinition
import com.intellij.ide.highlighter.XmlFileType
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * Indexes content of `.content.xml` files with *jcr:primaryType="cq:Component"*.
 *
 * @author Dmytro Troynikov
 */
class AemComponentDeclarationIndex : XmlIndex<AemComponentDefinition>() {

  companion object {
    val AEM_COMPONENT_DECLARATION_INDEX_ID: ID<String, AemComponentDefinition>
        = ID.create<String, AemComponentDefinition>("AemComponentDeclarationIndex")
  }

  override fun getValueExternalizer(): DataExternalizer<AemComponentDefinition>
      = AemComponentDeclarationExternalizer

  override fun getName(): ID<String, AemComponentDefinition>
      = AEM_COMPONENT_DECLARATION_INDEX_ID

  override fun getIndexer(): DataIndexer<String, AemComponentDefinition, FileContent>
      = AemComponentDeclarationIndexer

  override fun getInputFilter(): FileBasedIndex.InputFilter
      = FileBasedIndex.InputFilter {
    it.fileType == XmlFileType.INSTANCE
        && it.name == ".content.xml"
  }

}
