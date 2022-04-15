package com.aemtools.common.util

import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro Primshyts
 */

/**
 * Read an [XmlAttribute] as jcr property.
 *
 * @param name the name of jcr property
 * @param T the type of the jcr property
 *
 * @receiver [XmlTag]
 * @return the jcr value (_null_ if conversion is not possible)
 */
inline fun <reified T> XmlTag.jcrProperty(name: String): T? {
  val value = getAttribute(name)?.value ?: return null

  when {
    T::class == Boolean::class -> {
      return when (value) {
        "{Boolean}true", "true" -> true as T
        "{Boolean}false", "false" -> false as T
        else -> null
      }
    }
  }

  return null
}

/**
 * Read an [XmlAttribute] value as jcr property array.
 *
 * @param name name of jcr property
 *
 * @receiver [XmlTag]
 * @return list with jcr values, (_empty_ list if no property with given name found or
 * if the property is empty)
 */
fun XmlTag.jcrPropertyArray(name: String): List<String> {
  val unary = jcrProperty<String>(name)
  if (unary != null) {
    return listOf(unary)
  }

  val value = getAttribute(name)?.value ?: return emptyList()

  if (value.contains('[') && value.contains(']')) {
    return value.substringAfter('[')
        .substringBefore(']')
        .split(',')
        .map { it.trim() }
        .filterNot { it.isEmpty() }
  }

  return emptyList()
}
