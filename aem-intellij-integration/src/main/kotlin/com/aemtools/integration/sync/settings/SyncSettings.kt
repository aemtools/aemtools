package com.aemtools.integration.sync.settings

import com.aemtools.integration.sync.settings.gui.AemToolsConfigurationGUI
import com.aemtools.integration.sync.util.SyncConstants
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * @author Dmytro Liakhov
 */
class SyncSettings(private val project: Project) : SearchableConfigurable {

  private var configGUI: AemToolsConfigurationGUI? = null

  override fun getId(): String = SyncConstants.SETTINGS_ID

  override fun isModified(): Boolean = true

  override fun getDisplayName(): String = SyncConstants.DISPLAY_NAME_SETTINGS

  override fun apply() {
    val instanceInfo = AemToolsProjectConfiguration.getInstance(project)
    configGUI?.initModel(instanceInfo)
  }

  override fun reset() {}

  override fun getHelpTopic(): String? = SyncConstants.SETTINGS_ID

  override fun createComponent(): JComponent? {
    configGUI = AemToolsConfigurationGUI()
    val instanceInfo = AemToolsProjectConfiguration.getInstance(project)

    configGUI?.setUpForm(instanceInfo)
    return configGUI?.rootPanel
  }

  override fun disposeUIResources() {
    configGUI = null
  }

  companion object {

    /**
     * Get instance of [SyncSettings] associated with given project.
     *
     * @param project the project
     */
    fun getInstance(project: Project): SyncSettings = ServiceManager.getService(project, SyncSettings::class.java)
  }

}
