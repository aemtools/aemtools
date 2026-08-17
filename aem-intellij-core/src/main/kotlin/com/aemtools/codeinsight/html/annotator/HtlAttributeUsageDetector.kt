package com.aemtools.codeinsight.html.annotator

import com.aemtools.codeinsight.htl.model.HtlVariableDeclaration
import com.aemtools.codeinsight.htl.util.extractDeclarations
import com.aemtools.codeinsight.htl.util.filterForPosition
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.getHtmlFile
import com.aemtools.common.util.hasParentOfType
import com.aemtools.lang.htl.psi.HtlAccessIdentifier
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.util.getHtlFile
import com.aemtools.lang.util.isOption
import com.intellij.psi.xml.XmlAttribute

/**
 * Detects local HTL declaration usages without invoking project-wide reference search.
 */
object HtlAttributeUsageDetector {

  fun isUsed(attribute: XmlAttribute): Boolean {
    val declarations = HtlVariableDeclaration.create(attribute)
    if (declarations.isEmpty()) {
      return false
    }

    val htlFile = attribute.containingFile.getHtlFile()
        ?: return false
    val htmlFile = attribute.containingFile.getHtmlFile()
        ?: return false
    val allDeclarations = htmlFile.findChildrenByType(XmlAttribute::class.java)
        .toList()
        .extractDeclarations()

    return htlFile.findChildrenByType(VariableNameMixin::class.java)
        .asSequence()
        .filterNot { it.isOption() }
        .filterNot { it.hasParentOfType(HtlAccessIdentifier::class.java) }
        .any { variable ->
          val resolvedDeclaration = allDeclarations
              .filter { it.variableName == variable.variableName() }
              .filter { listOf(it).filterForPosition(variable).isNotEmpty() }
              .maxByOrNull { it.xmlAttribute.textRange.startOffset }

          declarations.any { declaration ->
            declaration.variableName == resolvedDeclaration?.variableName
                && declaration.xmlAttribute.isEquivalentTo(resolvedDeclaration.xmlAttribute)
          }
        }
  }

}
