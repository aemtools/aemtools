package com.aemtools.init

import com.intellij.codeInspection.htmlInspections.HtmlUnknownAttributeInspection
import com.intellij.codeInspection.htmlInspections.HtmlUnknownTagInspection
import com.intellij.openapi.application.ex.ApplicationManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager

/**
 * @author Dmytro Primshyts
 */
class AemIntellijStartupActivity : ProjectActivity {
  override suspend fun execute(project: Project) {
    val application = ApplicationManagerEx.getApplicationEx()
    if (application == null
        || application.isUnitTestMode
        || application.isHeadlessEnvironment) {
      return
    }

    runActivity(project)
  }

  private fun runActivity(project: Project) {
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
