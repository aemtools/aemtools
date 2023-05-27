package com.aemtools.codeinsight.osgiservice.property.provider.converter.impl

import com.aemtools.codeinsight.osgiservice.property.OSGiPropertyDescriptor
import com.aemtools.codeinsight.osgiservice.property.provider.converter.OSGiConfigPropertyConverter
import com.aemtools.common.constant.const.osgi.NO_PROPERTY_VALUE_SET
import com.aemtools.common.util.findChildrenByType
import com.aemtools.index.model.OSGiConfiguration
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiElement

class JsonOSGiConfigPropertyConverter : OSGiConfigPropertyConverter {
  override fun canConvert(osgiConfig: OSGiConfiguration): Boolean {
    return osgiConfig.file is JsonFile
  }

  override fun convert(osgiConfig: OSGiConfiguration, propertyName: String): OSGiPropertyDescriptor? {
    val file = osgiConfig.file as? JsonFile ?: return null

    val containingPsiElement: PsiElement? by lazy {
      file
          .findChildrenByType(JsonProperty::class.java)
          .find { it.name == propertyName }
    }

    return OSGiPropertyDescriptor(
        osgiConfig.mods.joinToString { it },
        osgiConfig.parameters[propertyName] ?: NO_PROPERTY_VALUE_SET,
        containingPsiElement,
        file
    )
  }
}
