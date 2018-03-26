package com.aemtools.init

import com.intellij.codeInspection.htmlInspections.HtmlUnknownAttributeInspection
import com.intellij.codeInspection.htmlInspections.HtmlUnknownTagInspection
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager

/**
 * @author Dmytro Troynikov
 */
class AemIntellijStartupActivity : StartupActivity {
  override fun runActivity(project: Project) {
    val currentProfile = ProjectInspectionProfileManager.getInstance(project).currentProfile
    currentProfile.modifyProfile {
      it.getInspectionTool("HtmlUnknownAttribute", project)
          ?.let {
            val htmlUnknownAttribute = it.tool
            if (htmlUnknownAttribute is HtmlUnknownAttributeInspection) {
              htmlUnknownAttribute.addEntry("x-cq-linkchecker")
            }
          }

      it.getInspectionTool("HtmlUnknownTag", project)
          ?.let {
            val htmlUnknownTag = it.tool

            if (htmlUnknownTag is HtmlUnknownTagInspection) {
              htmlUnknownTag.addEntry("sly")
            }
          }
    }
  }

}
