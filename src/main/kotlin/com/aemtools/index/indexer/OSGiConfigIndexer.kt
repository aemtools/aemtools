package com.aemtools.index.indexer

import com.aemtools.completion.util.getXmlFile
import com.aemtools.constant.const
import com.aemtools.constant.const.JCR_PRIMARY_TYPE
import com.aemtools.index.model.OSGiConfiguration
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

/**
 * @author Dmytro_Troynikov
 */
object OSGiConfigIndexer : DataIndexer<String, OSGiConfiguration, FileContent> {
  override fun map(inputData: FileContent): MutableMap<String, OSGiConfiguration> {
    val content = inputData.contentAsText

    if (content.contains(const.xml.JCR_PRIMARY_TYPE_OSGI_CONFIG)) {
      val file = inputData.psiFile.getXmlFile()
          ?: return mutableMapOf()

      val mainTag = file.rootTag
          ?: return mutableMapOf()

      val attributes = mainTag.attributes
          .filterNot {
            it.name == JCR_PRIMARY_TYPE
                || it.name == "xmlns:sling"
                || it.name == "xmlns:jcr"
          }
      val parameters = attributes.map {
        it.name to it.value
      }.toMap()
      val path = inputData.file.path
      return mutableMapOf(path to OSGiConfiguration(path, parameters))
    }
    return mutableMapOf()
  }
}
