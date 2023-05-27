package com.aemtools.codeinsight.osgiservice.property.provider.converter.impl

import com.aemtools.codeinsight.osgiservice.property.OSGiPropertyDescriptor
import com.aemtools.codeinsight.osgiservice.property.provider.converter.OSGiConfigPropertyConverter
import com.aemtools.common.constant.const.osgi.NO_PROPERTY_VALUE_SET
import com.aemtools.common.util.findChildrenByType
import com.aemtools.index.indexer.osgi.impl.JsonOSGiPropertyMapper
import com.aemtools.index.model.OSGiConfiguration
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty

class JsonOSGiConfigPropertyConverter : OSGiConfigPropertyConverter {
  override fun canConvert(osgiConfig: OSGiConfiguration): Boolean {
    return osgiConfig.file is JsonFile
  }

  override fun convert(osgiConfig: OSGiConfiguration, propertyName: String): OSGiPropertyDescriptor? {
    val file = osgiConfig.file as? JsonFile ?: return null
    val property = file
        .findChildrenByType(JsonProperty::class.java)
        .find { it.name == propertyName }

    val propertyValue = if (property != null) {
      JsonOSGiPropertyMapper.map(property).second ?: NO_PROPERTY_VALUE_SET
    } else {
      NO_PROPERTY_VALUE_SET
    }

    return OSGiPropertyDescriptor(
        osgiConfig.mods.joinToString { it },
        propertyValue,
        property,
        file
    )
  }
}
