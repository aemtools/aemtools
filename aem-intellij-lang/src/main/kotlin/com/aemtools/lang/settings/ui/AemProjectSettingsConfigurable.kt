package com.aemtools.lang.settings.ui

import com.aemtools.lang.settings.AemProjectSettings
import com.aemtools.lang.settings.ui.components.AemProjectSettingsPanel
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * @author Kostiantyn Diachenko
 */
class AemProjectSettingsConfigurable(val project: Project): Configurable {

  lateinit var component: AemProjectSettingsPanel

  override fun createComponent(): JComponent {
    val aemProjectSettings = AemProjectSettings.getInstance(project)
    component = AemProjectSettingsPanel(aemProjectSettings)
    return component.getPanel()
  }

  override fun isModified(): Boolean {
    val newState = component.getPanelState()
    val aemProjectSettings = AemProjectSettings.getInstance(project)
    return aemProjectSettings.aemVersion != newState.aemVersion
        || aemProjectSettings.htlVersion != newState.htlVersion
  }

  override fun apply() {
    val newState = component.getPanelState()
    val currentState = AemProjectSettings.getInstance(project)
    currentState.loadState(newState)
  }

  @Nls(capitalization = Nls.Capitalization.Title)
  override fun getDisplayName(): String {
    return "AEM Project Configuration"
  }
}
