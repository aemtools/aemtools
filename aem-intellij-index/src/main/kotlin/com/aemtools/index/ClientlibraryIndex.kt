package com.aemtools.index

import com.aemtools.index.dataexternalizer.ClientlibraryExternalizer
import com.aemtools.index.indexer.ClientlibraryIndexer
import com.aemtools.index.model.ClientlibraryModel
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * @author Dmytro Primshyts
 */
class ClientlibraryIndex : XmlIndex<ClientlibraryModel>() {
  companion object {
    val CLIENTLIBRARY_ID: ID<String, ClientlibraryModel> = ID.create<String, ClientlibraryModel>("ClientlibraryIndex")
  }

  override fun getValueExternalizer(): DataExternalizer<ClientlibraryModel> = ClientlibraryExternalizer()

  override fun getName(): ID<String, ClientlibraryModel> = CLIENTLIBRARY_ID

  override fun getIndexer(): DataIndexer<String, ClientlibraryModel, FileContent> = ClientlibraryIndexer

  override fun getInputFilter(): FileBasedIndex.InputFilter {
    return FileBasedIndex.InputFilter {
      it.name == ".content.xml"
    }
  }

}
