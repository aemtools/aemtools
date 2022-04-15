package com.aemtools.index.util

import com.aemtools.index.model.TemplateDefinition
import com.aemtools.lang.util.extractTemplateParameters
import com.intellij.psi.xml.XmlAttribute

/**
 * Extract [TemplateDefinition] from current [XmlAttribute].
 *
 * @receiver [XmlAttribute]
 * @return template definition, _null_ in case if current tag isn't of `data-sly-template` type.
 */
fun XmlAttribute.extractTemplateDefinition(): TemplateDefinition {
  val name = if (name.contains(".")) {
    name.substring(name.indexOf(".") + 1)
  } else {
    ""
  }

  val params = extractTemplateParameters()

  return TemplateDefinition(containingFile.virtualFile?.path, name, params)
}
