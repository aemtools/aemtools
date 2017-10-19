package com.aemtools.index.indexer

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.getXmlFile
import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.index.model.dialog.AemComponentTouchUIDialogDefinition
import com.aemtools.index.model.dialog.parameter.TouchUIDialogParameterDeclaration
import com.intellij.psi.xml.XmlTag
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

/**
 * @author Dmytro Troynikov
 */
object AemComponentTouchUIDialogIndexer : DataIndexer<String, AemComponentTouchUIDialogDefinition, FileContent> {
  override fun map(inputData: FileContent): MutableMap<String, AemComponentTouchUIDialogDefinition> {
    val file = inputData.psiFile.getXmlFile()
        ?: return mutableMapOf()

    val mainTag = file.rootTag
        ?: return mutableMapOf()

    val resourceType = inputData.file.path.normalizeToJcrRoot()
        .substringBefore("/_cq_dialog/")

    val dialogDefinition = AemComponentTouchUIDialogDefinition(
        inputData.file.path,
        resourceType,
        mainTag.findChildrenByType(XmlTag::class.java).mapNotNull {
          val slingResourceType = it.getAttribute("sling:resourceType")?.value
          val name = it.getAttribute("name")?.value
          if (slingResourceType != null && name != null) {
            TouchUIDialogParameterDeclaration(
                slingResourceType,
                name
            )
          } else {
            null
          }
        }
    )

    return mutableMapOf(
        resourceType to dialogDefinition
    )
  }
}
