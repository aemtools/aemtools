package com.aemtools.lang.settings.init

import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.toPsiFile
import com.aemtools.lang.settings.AemProjectSettings
import com.aemtools.lang.settings.model.AemVersion
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.settings.ui.AemProjectSettingsConfigurable
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag

/**
 * Startup activity to discover AEM project settings.
 *
 * @author Kostiantyn Diachenko
 */
class VersionsDiscoveringStartupActivity : StartupActivity {
  override fun runActivity(project: Project) {
    val aemProjectSettings = AemProjectSettings.getInstance(project)
    if (aemProjectSettings.isInitialized()) {
      return
    }

    val aemVersion = findAemVersion(project) ?: AemVersion.latest()
    saveDiscoveredVersions(aemVersion, aemProjectSettings)
    notifyAboutDiscoveredVersions(aemProjectSettings, project)
  }

  private fun saveDiscoveredVersions(aemVersion: AemVersion, aemProjectSettings: AemProjectSettings) {
    val newState = AemProjectSettings()
    newState.aemVersion = aemVersion
    newState.htlVersion = HtlVersion.getFirstCompatibleWith(aemVersion)
    aemProjectSettings.loadState(newState)
  }

  private fun findAemVersion(project: Project): AemVersion? {
    val xmlFiles = findPomFiles(project)
    return xmlFiles.flatMap {
      it.findChildrenByType(XmlTag::class.java)
    }.filter {
      it.isUberJarDependency()
    }.mapNotNull {
      val version = it.extractUberJarVersion()
      if (version != null) {
        AemVersion.fromVersion(version)
      } else {
        AemVersion.values().lastOrNull()
      }
    }.maxByOrNull { it.ordinal }
  }

  private fun XmlTag.extractUberJarVersion(): String? {
    val version = this.parentTag?.findFirstSubTag("version")
    return version?.value?.text
  }

  private fun XmlTag.isUberJarDependency(): Boolean =
      this.parentTag?.name == "dependency"
          && this.name == "artifactId"
          && this.value.text == "uber-jar"

  private fun notifyAboutDiscoveredVersions(aemProjectSettings: AemProjectSettings, project: Project) {
    val notification = Notification("Project Settings",
        "AEM Tools plugin configuration",
        """
            Discovered versions:
            <strong>AEM version</strong>: ${aemProjectSettings.aemVersion.version}
            <strong>HTL version</strong>: ${aemProjectSettings.htlVersion.version}<br>
          """.trimIndent(),
        NotificationType.INFORMATION
    )
    notification.addAction(setVersionsManuallyNotificationAction(project))
    Notifications.Bus.notify(notification)
  }

  private fun findPomFiles(project: Project): List<XmlFile> {
    val poms = FilenameIndex.getVirtualFilesByName(project, "pom.xml", GlobalSearchScope.projectScope(project))
    return poms.mapNotNull { it.toPsiFile(project) as? XmlFile }
  }

  private fun setVersionsManuallyNotificationAction(project: Project): NotificationAction {
    return object : NotificationAction("Set manually") {
      override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, AemProjectSettingsConfigurable::class.java)
        notification.expire()
      }
    }
  }
}
