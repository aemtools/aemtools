package com.aemtools.codeinsight.osgiservice.property.provider.converter

import com.aemtools.codeinsight.osgiservice.property.OSGiPropertyDescriptor
import com.aemtools.index.model.OSGiConfiguration

interface OSGiConfigPropertyConverter {
  fun canConvert(osgiConfig: OSGiConfiguration): Boolean
  fun convert(osgiConfig: OSGiConfiguration, propertyName: String): OSGiPropertyDescriptor?
}
