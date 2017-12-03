package com.aemtools.inspection.html

import com.aemtools.common.constant.const
import com.aemtools.common.util.toSmartPointer
import com.aemtools.inspection.html.fix.SubstituteWithRawAttributeIntentionAction
import com.aemtools.lang.util.htlAttributeName
import com.intellij.codeInsight.daemon.impl.analysis.RemoveAttributeIntentionFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.htmlInspections.HtmlLocalInspectionTool
import com.intellij.psi.xml.XmlAttribute

/**
 * Checks if data-sly-attribute is used with prohibited attribute
 * (e.g. "style").
 *
 * @author Dmytro Troynikov
 */
class MessedDataSlyAttributeInspection : HtmlLocalInspectionTool() {
  override fun getGroupDisplayName(): String = "HTL"

  override fun getDisplayName(): String = "data-sly-attribute with prohibited attributes"

  override fun getStaticDescription(): String? {
    return """
<html>
<body>
This inspection verifies that <i>data-sly-attribute</i> is
<b>not</b> used with prohibited attributes, such as <b>style</b> or event attributes i.e.
attributes that take JavaScript as input (e.g. onclick, onmousemove, etc).
</body>
</html>
    """.trimIndent()
  }

  public override fun checkAttribute(attribute: XmlAttribute,
                                     holder: ProblemsHolder,
                                     isOnTheFly: Boolean) {
    val htlAttributeName = attribute.htlAttributeName()
    if (htlAttributeName != const.htl.DATA_SLY_ATTRIBUTE) {
      return
    }

    val htlVariableName = attribute.name.substringAfter(".")
    if (htlVariableName == "style" || htlVariableName in const.html.JS_ATTRIBUTES) {
      holder.registerProblem(
          attribute,
          "$htlVariableName is not allowed in data-sly-attribute",
          ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
          RemoveAttributeIntentionFix(
              attribute.name, attribute
          ),
          SubstituteWithRawAttributeIntentionAction(
              attribute.toSmartPointer(),
              "Replace with: $htlVariableName=\"${attribute.value}\""
          )
      )
    }
  }

}
