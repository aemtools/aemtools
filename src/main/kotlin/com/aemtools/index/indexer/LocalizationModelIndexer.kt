package com.aemtools.index.indexer

import com.aemtools.completion.util.getXmlFile
import com.aemtools.constant.const
import com.aemtools.index.model.LocalizationModel
import com.intellij.psi.xml.XmlTag
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

/**
 * @author Dmytro Troynikov
 */
object LocalizationModelIndexer : DataIndexer<String, LocalizationModel, FileContent> {

  override fun map(inputData: FileContent): MutableMap<String, LocalizationModel> {
    val xmlFile = inputData.psiFile.getXmlFile()
        ?: return mutableMapOf()

    val rootTag = xmlFile.rootTag
        ?: return mutableMapOf()

    if (!containsLanguageMixin(rootTag)) {
      return mutableMapOf()
    }

    val fileName = inputData.file.path

    val jcrLanguage = rootTag.attributes.find {
      it.name == const.xml.JCR_LANGUAGE
    }
        ?.value
        ?: return mutableMapOf()

    val models = rootTag.subTags
        .mapNotNull {
          LocalizationModel.create(it,
              fileName,
              jcrLanguage)
        }

    return mutableMapOf(
        *models.map { model ->
          "${model.fileName}#${model.key}" to model
        }.toTypedArray()
    )
  }

  private fun containsLanguageMixin(rootTag: XmlTag): Boolean {
    return rootTag.attributes.none {
      it.name == const.xml.JCR_MIXIN_TYPES
          && it.value?.contains(const.xml.JCR_LANGUAGE) == true
    }
  }

}
