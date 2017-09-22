package com.aemtools.index.indexer

import com.aemtools.completion.util.extractTemplateDefinition
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.getHtmlFile
import com.aemtools.constant.const
import com.aemtools.index.model.TemplateDefinition
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

/**
 * @author Dmytro Troynikov
 */
object HtlTemplateIndexer : DataIndexer<String, TemplateDefinition, FileContent> {
  override fun map(inputData: FileContent): MutableMap<String, TemplateDefinition> {
    val content = inputData.contentAsText

    if (content.contains(const.htl.DATA_SLY_TEMPLATE)) {

      val file = inputData.psiFile.getHtmlFile()
          ?: return mutableMapOf()
      val attributes = file.findChildrenByType(XmlAttribute::class.java)

      val templates = attributes.filter { it.name.startsWith(const.htl.DATA_SLY_TEMPLATE) }

      val templateDefinitions: List<TemplateDefinition> = templates.flatMap {
        with(it.extractTemplateDefinition()) {
          if (this != null) {
            listOf(this)
          } else {
            listOf()
          }
        }
      }
      val path = inputData.file.path
      templateDefinitions.forEach { it.fullName = path }

      return mutableMapOf(*templateDefinitions.map {
        "$path.$${it.name}" to it
      }.toTypedArray())
    }
    return mutableMapOf()
  }
}
