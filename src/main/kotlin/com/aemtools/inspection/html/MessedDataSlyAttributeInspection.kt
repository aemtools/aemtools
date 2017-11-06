package com.aemtools.inspection.html

import com.aemtools.completion.util.htlAttributeName
import com.aemtools.completion.util.toSmartPointer
import com.aemtools.constant.const
import com.aemtools.lang.html.inspection.MessedSlyAttributeAnnotator
import com.intellij.codeInsight.daemon.impl.analysis.RemoveAttributeIntentionFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.htmlInspections.HtmlLocalInspectionTool
import com.intellij.psi.xml.XmlAttribute

/**
 * Checks if data-sly-attribute is used with prohibited attribute
 * e.g. "style".
 *
 * @author Dmytro Troynikov
 */
class MessedDataSlyAttributeInspection : HtmlLocalInspectionTool() {
  override fun getGroupDisplayName(): String {
    return "HTL Inspections"
  }

  override fun getDisplayName(): String {
    return "data-sly-attribute with prohibited attributes"
  }

  override fun getDescriptionFileName(): String? {

      return "MessedDataSlyAttributeInspection.html"
  }

  override fun getID(): String {
    return super.getID()
  }

  override fun getStaticDescription(): String? {
    return super.getStaticDescription()
  }

  override fun getShortName(): String {
    return super.getShortName()
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
          MessedSlyAttributeAnnotator.SubstituteWithRawAttributeFix(
              attribute.toSmartPointer(),
              "Replace with: $htlVariableName=\"${attribute.value}\""
          )
      )
    }
  }

}
