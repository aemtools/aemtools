package com.aemtools.codeinsight.osgiservice.property.provider

import com.aemtools.codeinsight.osgiservice.markerinfo.OSGiPropertyDescriptor
import com.aemtools.common.util.findChildrenByType
import com.aemtools.index.model.OSGiConfiguration
import com.aemtools.index.model.sortByMods
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.psi.PsiClass
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Kostiantyn Diachenko
 */
object OSGiPropertyDescriptorsProvider {
  fun get(referencedOsgiComponentClass: PsiClass, configPropertyName: String): List<OSGiPropertyDescriptor> {
    val containingClassFqn = referencedOsgiComponentClass.qualifiedName
        ?: return emptyList()

    val configs = OSGiConfigSearch.findConfigsForClass(
        containingClassFqn,
        referencedOsgiComponentClass.project,
        true)
    if (configs.isEmpty()) {
      return emptyList()
    }

    return propertyDescriptors(configs, configPropertyName)
  }

  private fun propertyDescriptors(configs: List<OSGiConfiguration>,
                                  value: String): List<OSGiPropertyDescriptor> {
    return configs.sortByMods()
        .mapNotNull { config ->
          val file = config.xmlFile ?: return@mapNotNull null
          val attribute = file
              .findChildrenByType(XmlAttribute::class.java)
              .find { it.name == value }

          val attributeValue = attribute?.value ?: "<no value set>"

          OSGiPropertyDescriptor(
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

  private fun padModsByMaxModLength(propertyDescriptors: List<OSGiPropertyDescriptor>)
      : List<OSGiPropertyDescriptor> {
    val modsMaxLength = propertyDescriptors
        .maxByOrNull {
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
