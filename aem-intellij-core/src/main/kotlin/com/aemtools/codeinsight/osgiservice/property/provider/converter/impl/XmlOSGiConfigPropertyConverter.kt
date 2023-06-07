package com.aemtools.codeinsight.osgiservice.property.provider.converter.impl

import com.aemtools.codeinsight.osgiservice.property.OSGiPropertyDescriptor
import com.aemtools.codeinsight.osgiservice.property.provider.converter.OSGiConfigPropertyConverter
import com.aemtools.common.constant.const.osgi.NO_PROPERTY_VALUE_SET
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.toNavigatable
import com.aemtools.index.model.OSGiConfiguration
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile

class XmlOSGiConfigPropertyConverter : OSGiConfigPropertyConverter {
  override fun canConvert(osgiConfig: OSGiConfiguration): Boolean {
    return osgiConfig.file is XmlFile
  }

  override fun convert(osgiConfig: OSGiConfiguration, propertyName: String): OSGiPropertyDescriptor? {
    val xmlFile = osgiConfig.file as? XmlFile ?: return null
    val containingPsiElement: PsiElement? by lazy {
      xmlFile
          .findChildrenByType(XmlAttribute::class.java)
          .find { it.name == propertyName }
          ?.toNavigatable()
    }

    return OSGiPropertyDescriptor(
        osgiConfig.mods.joinToString { it },
        osgiConfig.parameters[propertyName] ?: NO_PROPERTY_VALUE_SET,
        containingPsiElement,
        xmlFile
    )
  }
}
