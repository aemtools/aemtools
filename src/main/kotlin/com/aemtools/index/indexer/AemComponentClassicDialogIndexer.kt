package com.aemtools.index.indexer

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.getXmlFile
import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.constant.const.xml.JCR_PRIMARY_TYPE_CQ_DIALOG
import com.aemtools.index.model.dialog.AemComponentClassicDialogDefinition
import com.aemtools.index.model.dialog.parameter.ClassicDialogParameterDeclaration
import com.intellij.psi.xml.XmlTag
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

/**
 * @author Dmytro Troynikov
 */
object AemComponentClassicDialogIndexer : DataIndexer<String, AemComponentClassicDialogDefinition, FileContent> {
  override fun map(inputData: FileContent): MutableMap<String, AemComponentClassicDialogDefinition> {
    val content = inputData.contentAsText

    if (content.contains(JCR_PRIMARY_TYPE_CQ_DIALOG)) {
      val file = inputData.psiFile.getXmlFile()
          ?: return mutableMapOf()

      val mainTag = file.rootTag
          ?: return mutableMapOf()

      val resourceType = inputData.file.path.normalizeToJcrRoot()
          .substringBeforeLast("/")

      val dialogDefinition = AemComponentClassicDialogDefinition(
          inputData.file.path,
          resourceType,
          mainTag.findChildrenByType(XmlTag::class.java).mapNotNull {
            val xtype = it.getAttribute("xtype")?.value
            val name = it.getAttribute("name")?.value
            if (xtype != null && name != null) {
              ClassicDialogParameterDeclaration(xtype, name)
            } else {
              null
            }
          }
      )

      return mutableMapOf(
          resourceType to dialogDefinition
      )
    }

    return mutableMapOf()
  }
}
