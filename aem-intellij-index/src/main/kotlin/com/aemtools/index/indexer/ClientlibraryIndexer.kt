package com.aemtools.index.indexer

import com.aemtools.common.util.getXmlFile
import com.aemtools.index.model.ClientlibraryModel
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

/**
 * @author Dmytro Primshyts
 */
object ClientlibraryIndexer : DataIndexer<String, ClientlibraryModel, FileContent> {
  override fun map(inputData: FileContent): MutableMap<String, ClientlibraryModel> {
    val content = inputData.contentAsText

    if (content.contains("jcr:primaryType=\"cq:ClientLibraryFolder\"")) {
      val file = inputData.psiFile.getXmlFile()
          ?: return mutableMapOf()

      val mainTag = file.rootTag
          ?: return mutableMapOf()

      val path = inputData.file.path
      val model = ClientlibraryModel.fromTag(mainTag)
          ?: return mutableMapOf()

      return mutableMapOf(
          path to model
      )
    }

    return mutableMapOf()
  }
}
