package com.aemtools.sync.settings

import com.aemtools.sync.settings.gui.AEMToolsConfigurationGUI
import com.aemtools.sync.util.SyncConstants
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * @author Dmytro Liakhov
 */
class SyncSettings(val project: Project) : SearchableConfigurable {

  private var configGUI: AEMToolsConfigurationGUI? = null

  override fun getId(): String = SyncConstants.SETTINGS_ID

  override fun isModified(): Boolean = true

  override fun getDisplayName(): String = SyncConstants.DISPLAY_NAME_SETTINGS

  override fun apply() {
    val instanceInfo = InstanceInfo.getInstance(project)
    configGUI?.initModel(instanceInfo)
  }

  override fun reset() {}

  override fun getHelpTopic(): String? = SyncConstants.SETTINGS_ID

  override fun createComponent(): JComponent? {
    configGUI = AEMToolsConfigurationGUI()
    val instanceInfo = InstanceInfo.getInstance(project)
    configGUI?.setUpForm(instanceInfo)
    return configGUI?.getRootPanel()
  }

  override fun disposeUIResources() {
    configGUI = null
  }

  companion object {
    fun getInstance(project: Project): SyncSettings = ServiceManager.getService(project, SyncSettings::class.java)
  }

}
