package com.aemtools.inspection.html

import com.aemtools.common.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.inspection.service.InspectionService
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.htmlInspections.HtmlLocalInspectionTool
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Primshyts
 */
class RedundantDataSlyUnwrapInspection : HtmlLocalInspectionTool() {
  override fun getGroupDisplayName(): String = "HTL"

  override fun getDisplayName(): String
      = "data-sly-unwrap is redundant inside sly tag"

  override fun getStaticDescription(): String? {
    return """
<html>
<body>
This inspection verifies that <i>data-sly-unwrap</i> is
<b>not</b> used inside of <i>sly</i> tag
</body>
</html>
        """.trimIndent()
  }

  public override fun checkAttribute(attribute: XmlAttribute, holder: ProblemsHolder, isOnTheFly: Boolean) {
    val inspectionService = InspectionService.getInstance(attribute.project)
        ?: return
    if (!inspectionService.validTarget(attribute)) {
      return
    }

    if (attribute.text == DATA_SLY_UNWRAP
        && attribute.parent?.name?.equals("sly", true) == true) {
      inspectionService.redundantDataSlyUnwrap(holder, attribute)
    }
  }
}
