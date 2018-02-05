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

  private var initializedInstanceInfo: InstanceInfo? = null

  private var configGUI: AEMToolsConfigurationGUI? = null

  override fun getId(): String = SyncConstants.SETTINGS_ID

  override fun isModified(): Boolean {
    val instanceInfo = configGUI?.initModel(InstanceInfo.getInstance(project))
    return initializedInstanceInfo != instanceInfo
  }

  override fun getDisplayName(): String = SyncConstants.DISPLAY_NAME_SETTINGS

  override fun apply() {
    val instanceInfo = InstanceInfo.getInstance(project)
    configGUI?.initModel(instanceInfo)
    initializedInstanceInfo = instanceInfo.copy()
  }

  override fun reset() {}

  override fun getHelpTopic(): String? = SyncConstants.SETTINGS_ID

  override fun createComponent(): JComponent? {
    configGUI = AEMToolsConfigurationGUI()
    val instanceInfo = InstanceInfo.getInstance(project)


    initializedInstanceInfo = instanceInfo.copy()
    configGUI?.setUpForm(instanceInfo)
    return configGUI?.getRootPanel()
  }

  override fun disposeUIResources() {
    configGUI = null
    initializedInstanceInfo = null
  }

  companion object {
    fun getInstance(project: Project): SyncSettings = ServiceManager.getService(project, SyncSettings::class.java)
  }

}
