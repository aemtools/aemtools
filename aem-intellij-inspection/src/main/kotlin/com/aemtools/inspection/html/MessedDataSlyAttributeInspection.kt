package com.aemtools.inspection.html

import com.aemtools.common.constant.const
import com.aemtools.inspection.service.InspectionService
import com.aemtools.lang.util.htlAttributeName
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.htmlInspections.HtmlLocalInspectionTool
import com.intellij.psi.xml.XmlAttribute

/**
 * Checks if data-sly-attribute is used with prohibited attribute
 * (e.g. "style").
 *
 * @author Dmytro Primshyts
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
    val inspectionService = InspectionService.getInstance(attribute.project)
        ?: return
    if (!inspectionService.validTarget(attribute)) {
      return
    }

    val htlAttributeName = attribute.htlAttributeName()
    if (htlAttributeName != const.htl.DATA_SLY_ATTRIBUTE) {
      return
    }

    val htlVariableName = attribute.name.substringAfter(".")
    if (htlVariableName == "style" || htlVariableName in const.html.JS_ATTRIBUTES) {
      inspectionService.messedDataSlyAttribute(
          holder,
          attribute,
          htlVariableName
      )
    }
  }

}
