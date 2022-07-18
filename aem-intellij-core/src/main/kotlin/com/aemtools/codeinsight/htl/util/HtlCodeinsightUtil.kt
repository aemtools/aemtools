package com.aemtools.codeinsight.htl.util

import com.aemtools.codeinsight.htl.model.DeclarationAttributeType
import com.aemtools.codeinsight.htl.model.HtlVariableDeclaration
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.getHtmlFile
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.util.isAfterDeclaration
import com.aemtools.lang.htl.psi.util.isNotPartOf
import com.aemtools.lang.htl.psi.util.isPartOf
import com.aemtools.lang.htl.psi.util.isWithin
import com.aemtools.lang.util.isHtlDeclarationAttribute
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

/**
 * Extract list of Htl variable declarations from current [XmlAttribute] collection.
 * @receiver [Collection] of [XmlAttribute] objects
 * @return collection of [HtlVariableDeclaration] elements
 */
fun List<XmlAttribute>.extractDeclarations(): List<HtlVariableDeclaration> {
  return filter { it.isHtlDeclarationAttribute() }
      .flatMap {
        HtlVariableDeclaration.create(it)
      }
}

/**
 * Collection [HtlVariableDeclaration] element which are applicable for given position
 * @param position the starting position
 * @receiver [List] of [HtlVariableDeclaration] objects
 * @return collection of applicable declarations
 */
fun List<HtlVariableDeclaration>.filterForPosition(position: PsiElement): List<HtlVariableDeclaration> {
  val applicableDeclarations = this.filter {
    when (it.attributeType) {
      DeclarationAttributeType.DATA_SLY_USE,
      DeclarationAttributeType.DATA_SLY_TEST,
      DeclarationAttributeType.DATA_SLY_UNWRAP,
      DeclarationAttributeType.DATA_SLY_SET -> {
        position.isAfterDeclaration(it.xmlAttribute)
      }
      DeclarationAttributeType.DATA_SLY_LIST,
      DeclarationAttributeType.LIST_HELPER -> {
        val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

        position.isWithin(tag)
      }
      DeclarationAttributeType.DATA_SLY_REPEAT,
      DeclarationAttributeType.REPEAT_HELPER -> {
        val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

        position.isPartOf(tag) && position.isNotPartOf(it.xmlAttribute)
      }
      DeclarationAttributeType.DATA_SLY_TEMPLATE_PARAMETER -> {
        val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

        position.isWithin(tag)
      }

      DeclarationAttributeType.DATA_SLY_TEMPLATE -> {
        val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false
        !position.isPartOf(tag)
      }
    }
  }

  val groupedByName = applicableDeclarations.groupBy { it.variableName }
  val result = if (groupedByName.values.find { it.size > 1 } != null) {

    groupedByName.values.flatMap {
      if (it.size == 1) {
        it
      } else {

        val parentTags = position.run {
          val html = position.containingFile.getHtmlFile() ?: return@run listOf<XmlTag>()
          val parent = position.findParentByType(HtlHtlEl::class.java) ?: return@run listOf<XmlTag>()
          val offset = parent.textOffset - 1
          val htmlElement = html.findElementAt(offset)

          var currentElement = htmlElement.findParentByType(XmlTag::class.java)
          val result = ArrayList<XmlTag>()

          while (currentElement != null) {
            result.add(currentElement)
            currentElement = currentElement.prevSibling.findParentByType(XmlTag::class.java)
          }
          result
        }

        val closest = it.minByOrNull {
          val myTag = it.xmlAttribute.findParentByType(XmlTag::class.java)
              ?: return@minByOrNull 100

          val myIndex = parentTags.indexOf(myTag)
          return@minByOrNull if (myIndex > -1) {
            myIndex
          } else {
            100
          }
        } ?: return@flatMap listOf<HtlVariableDeclaration>()

        listOf(closest)
      }
    }

  } else {
    applicableDeclarations
  }
  return result
}
