package com.aemtools.inspection.html

import com.aemtools.completion.util.htlAttributeName
import com.aemtools.completion.util.toSmartPointer
import com.aemtools.constant.const
import com.aemtools.inspection.fix.SubstituteWithRawAttributeIntentionAction
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
  override fun getGroupDisplayName(): String {
    return "HTL"
  }

  override fun getDisplayName(): String {
    return "data-sly-attribute with prohibited attributes"
  }

  override fun getStaticDescription(): String? {
    return """
<html>
<body>
This inspection verifies that <i>data-sly-attribute</i> is
<b>not</b> used with prohibited attributes, such as <b>style</b> or event attributes i.e.
attributes that take JavaScript as input (e.g. onclick, onmousemove, etc).
</body>
</html>
    """
  }

  override fun checkAttribute(attribute: XmlAttribute,
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
