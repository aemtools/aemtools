package com.aemtools.sync.settings

import com.aemtools.sync.settings.gui.AEMToolsConfigurationGUI
import com.aemtools.sync.settings.model.InstanceInfoModel
import com.aemtools.sync.util.SyncConstants.DISPLAY_NAME_SETTINGS
import com.aemtools.sync.util.SyncConstants.SETTINGS_ID
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * @author Dmytro Liakhov
 */
class SyncSettings(val project: Project) : SearchableConfigurable {

  private var configGUI: AEMToolsConfigurationGUI? = null

  override fun getId(): String = SETTINGS_ID

  override fun isModified(): Boolean = true

  override fun getDisplayName(): String = DISPLAY_NAME_SETTINGS

  override fun apply() {
    val login: String = configGUI?.login ?: ""
    val password = configGUI?.password ?: ""
    val url = configGUI?.urlInstance ?: ""
    val enabled = configGUI?.enabled ?: false

    val instanceInfo = InstanceInfoModel(enabled, url, login, password)
    val instanceService = InstanceInfoService.getInstance(project)

    instanceService.instanceInfoModel = instanceInfo
  }

  override fun reset() {
    println("Reset")
  }

  override fun getHelpTopic(): String? = SETTINGS_ID

  override fun createComponent(): JComponent? {
    configGUI = AEMToolsConfigurationGUI()
    return configGUI?.getRootPanel()
  }

  override fun disposeUIResources() {
    configGUI = null
  }

  companion object {
    fun getInstance(project: Project): SyncSettings = ServiceManager.getService(project, SyncSettings::class.java)
  }

}