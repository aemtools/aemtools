package com.aemtools.codeinsight.osgiservice.property.mapper

import java.util.regex.Pattern

/**
 * @author Kostiantyn Diachenko
 */
object OSGiComponentPropertyNameMapper {

  private val METHOD_NAME_UTIL_SYMBOLS_PATTERN =
      Pattern.compile("(__)|(_)|(\\$\\_\\$)|(\\$\\$)|(\\$)")
  private val COMPONENT_PROPERTY_SYMBOLS_MAPPING = mapOf(
      "__" to "_",
      "_" to ".",
      "\$_\$" to "-",
      "\$\$" to "\$",
      "\$" to ""
  )

  /**
   * Coverts OSGi component property type method name to OSGi component property name.
   * Certain common property name characters, such as full stop ('.' \u002E)
   * and hyphen-minus ('-' \u002D) are not valid in Java identifiers.
   *
   * @param methodName component property type method name
   * @return component property name
   */
  fun mapByMethodName(methodName: String): String {
    val matcher = METHOD_NAME_UTIL_SYMBOLS_PATTERN.matcher(methodName)
    var propertyName = ""
    var startIndex = 0
    while (matcher.find()) {
      propertyName += methodName.substring(startIndex, matcher.start())
      val utilSymbol = matcher.group()
      propertyName += COMPONENT_PROPERTY_SYMBOLS_MAPPING.getOrDefault(utilSymbol, utilSymbol)
      startIndex = matcher.end()
    }
    if (startIndex == 0) {
      return methodName
    }
    propertyName += methodName.substring(startIndex, methodName.length)
    return propertyName
  }
}
