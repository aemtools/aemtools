package com.aemtools.index.indexer

import com.aemtools.completion.util.getXmlFile
import com.aemtools.constant.const.xml.JCR_PRIMARY_TYPE_CQ_COMPONENT
import com.aemtools.index.model.AemComponentDefinition
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

/**
 * @author Dmytro Troynikov
 */
object AemComponentDeclarationIndexer : DataIndexer<String, AemComponentDefinition, FileContent> {
  override fun map(inputData: FileContent): MutableMap<String, AemComponentDefinition> {
    val content = inputData.contentAsText

    if (content.contains(JCR_PRIMARY_TYPE_CQ_COMPONENT)) {
      val file = inputData.psiFile.getXmlFile()
          ?: return mutableMapOf()

      val mainTag = file.rootTag
          ?: return mutableMapOf()

      val key = inputData.file.path
      val aemComponentDefinition = AemComponentDefinition.fromTag(mainTag, key)
          ?: return mutableMapOf()

      return mutableMapOf(key to aemComponentDefinition)
    }
    return mutableMapOf()
  }
}
