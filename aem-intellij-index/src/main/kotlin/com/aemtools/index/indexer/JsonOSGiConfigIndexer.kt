package com.aemtools.index.indexer

import com.aemtools.index.indexer.osgi.impl.JsonOSGiPropertyMapper
import com.aemtools.index.model.OSGiConfigurationIndexModel
import com.intellij.json.JsonLanguage
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

object JsonOSGiConfigIndexer : DataIndexer<String, OSGiConfigurationIndexModel, FileContent> {
  override fun map(inputData: FileContent): MutableMap<String, OSGiConfigurationIndexModel> {
    val content = inputData.contentAsText.trim()

    if (!content.startsWith("{") || !content.endsWith("}")) {
      return mutableMapOf()
    }

    val jsonFile = inputData.psiFile.viewProvider.getPsi(JsonLanguage.INSTANCE) as? JsonFile
        ?: return mutableMapOf()

    val jsonObject = jsonFile.topLevelValue as? JsonObject
        ?: return mutableMapOf()

    val parameters = jsonObject.propertyList
        .map { JsonOSGiPropertyMapper.map(it) }
        .toMap()

    val path = inputData.file.path
    return mutableMapOf(path to OSGiConfigurationIndexModel(path, parameters))
  }
}
