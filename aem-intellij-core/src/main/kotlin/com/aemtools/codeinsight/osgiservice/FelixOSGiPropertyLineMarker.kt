package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.FelixOSGiPropertyDescriptor
import com.aemtools.codeinsight.osgiservice.markerinfo.FelixOSGiPropertyMarkerInfo
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.isFelixProperty
import com.aemtools.common.util.isOSGiService
import com.aemtools.index.model.OSGiConfiguration
import com.aemtools.index.model.sortByMods
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Primshyts
 */
class FelixOSGiPropertyLineMarker : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
    val identifier = element as? PsiIdentifier
        ?: return null
    val containingClass = identifier.findParentByType(PsiClass::class.java)
        ?: return null
    val field = identifier.parent as? PsiField
        ?: return null

    if (containingClass.isOSGiService()
        && field.isFelixProperty()) {

      val value = field.computeConstantValue() as? String
          ?: return null

      val containingClassFqn = containingClass.qualifiedName ?: return null

      val configs = OSGiConfigSearch.findConfigsForClass(
          containingClassFqn,
          element.project,
          true)
      if (configs.isEmpty()) {
        return null
      }

      return FelixOSGiPropertyMarkerInfo(element) {
        val containingClassFqn = containingClass.qualifiedName
            ?: return@FelixOSGiPropertyMarkerInfo emptyList()

        val configs = OSGiConfigSearch.findConfigsForClass(
            containingClassFqn,
            element.project,
            true)
        if (configs.isEmpty()) {
          return@FelixOSGiPropertyMarkerInfo emptyList()
        }

        propertyDescriptors(configs, value)
      }
    }

    return null
  }

  override fun collectSlowLineMarkers(elements: MutableList<PsiElement>,
                                      result: MutableCollection<LineMarkerInfo<PsiElement>>) {
  }

  private fun propertyDescriptors(configs: List<OSGiConfiguration>, value: String): List<FelixOSGiPropertyDescriptor> {
    return configs.sortByMods()
        .mapNotNull { config ->
          val file = config.xmlFile ?: return@mapNotNull null
          val attribute = file
              .findChildrenByType(XmlAttribute::class.java)
              .find { it.name == value }

          val attributeValue = attribute?.value ?: "<no value set>"

          FelixOSGiPropertyDescriptor(
              config.mods.joinToString { it },
              attributeValue,
              attribute,
              file
          )
        }
        .let { propertyDescriptors ->
          padModsByMaxModLength(propertyDescriptors)
        }
  }

  private fun padModsByMaxModLength(propertyDescriptors: List<FelixOSGiPropertyDescriptor>)
      : List<FelixOSGiPropertyDescriptor> {
    val modsMaxLength = propertyDescriptors
        .maxBy {
          it.mods.length
        }?.mods?.length
        ?: 0
    return propertyDescriptors.map {
      it.copy(
          mods = it.mods.padEnd(
              modsMaxLength
          )
      )
    }
  }

}
