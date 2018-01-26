package com.aemtools.sync.settings

import com.aemtools.sync.settings.gui.AEMToolsConfigurationGUI
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

/**
 * @author Dmytro Liakhov
 */
class SyncSettings : SearchableConfigurable {
  private var configGUI: AEMToolsConfigurationGUI? = null

  override fun getId(): String = "preference.AEMTools"

  override fun isModified(): Boolean = true

  override fun getDisplayName(): String = "AEM Tools"

  override fun apply() {
    println("Applied!")
  }

  override fun reset() {
    println("Reset")
  }

  override fun getHelpTopic(): String? = "preference.AEMTools"

  override fun createComponent(): JComponent? {
    configGUI = AEMToolsConfigurationGUI()
    return configGUI?.labelEnableSync
  }

  override fun disposeUIResources() {
    configGUI = null
  }

}