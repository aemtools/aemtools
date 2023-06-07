package com.aemtools.codeinsight.osgiservice.property.provider

import com.aemtools.codeinsight.osgiservice.property.OSGiPropertyDescriptor
import com.aemtools.codeinsight.osgiservice.property.provider.converter.OSGiConfigPropertyConverter
import com.aemtools.codeinsight.osgiservice.property.provider.converter.impl.JsonOSGiConfigPropertyConverter
import com.aemtools.codeinsight.osgiservice.property.provider.converter.impl.XmlOSGiConfigPropertyConverter
import com.aemtools.index.model.OSGiConfiguration
import com.aemtools.index.model.sortByMods
import com.aemtools.index.search.OSGiConfigSearch
import com.intellij.psi.PsiClass

/**
 * @author Kostiantyn Diachenko
 */
object OSGiPropertyDescriptorsProvider {
  private val converters: List<OSGiConfigPropertyConverter> = listOf(
      XmlOSGiConfigPropertyConverter(),
      JsonOSGiConfigPropertyConverter(),
  )

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
          toPropertyDescriptor(config, value)
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

  private fun toPropertyDescriptor(osgiConfiguration: OSGiConfiguration, propertyName: String)
      : OSGiPropertyDescriptor? {
    return converters.find { it.canConvert(osgiConfiguration) }
        ?.convert(osgiConfiguration, propertyName)
  }
}
