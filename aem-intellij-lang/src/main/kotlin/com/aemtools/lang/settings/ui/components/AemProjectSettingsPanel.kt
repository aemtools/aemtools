package com.aemtools.lang.settings.ui.components

import com.aemtools.lang.settings.AemProjectSettings
import com.aemtools.lang.settings.model.AemVersion
import com.aemtools.lang.settings.model.HtlVersion
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*

/**
 * @author Kostiantyn Diachenko
 */
class AemProjectSettingsPanel(private val currentState: AemProjectSettings) {

  lateinit var isSetHtlVersionManuallyCheckbox: Cell<JBCheckBox>
  lateinit var htlVersionComboBox: Cell<ComboBox<String>>

  var newAemVersion = AtomicProperty(currentState.aemVersion.version)
  var newHtlVersion = AtomicProperty(currentState.htlVersion.version)
  var isManuallyDefinedHtlVersion = AtomicProperty(currentState.isManuallyDefinedHtlVersion)

  fun getPanel(): DialogPanel = panel {
    row("AEM Version:") {
      comboBox(AemVersion.versions())
          .comment("Select AEM version of the current project")
          .gap(RightGap.SMALL)
          .bindItem(newAemVersion)
          .whenItemSelectedFromUi { aemVersion ->
            if (isManuallyDefinedHtlVersion.get()) {
              return@whenItemSelectedFromUi
            }
            val selectedAemVersion = AemVersion.fromFullVersion(aemVersion)
            selectedAemVersion?.apply {
              val suggestedHtlVersion = HtlVersion.getFirstCompatibleWith(selectedAemVersion)
              newHtlVersion.set(suggestedHtlVersion.version)
            }
          }
    }
    buttonsGroup {
      row {
        isSetHtlVersionManuallyCheckbox = checkBox("Set HTL versions manually")
            .gap(RightGap.SMALL)
            .comment("""
              By default, it is set automatically based on AEM version.
              An implementation of version 1.4 of the HTL is available in AEM 6.3 SP3 and AEM 6.4 SP1.
            """.trimIndent())
            .bindSelected(isManuallyDefinedHtlVersion)
            .whenStateChangedFromUi { isChecked ->
              if (isChecked) {
                return@whenStateChangedFromUi
              }
              if (newAemVersion.get() == currentState.aemVersion.version) {
                newHtlVersion.set(currentState.htlVersion.version)
              }
              if (newAemVersion.get() != currentState.aemVersion.version) {
                val selectedAemVersion = AemVersion.fromFullVersion(newAemVersion.get())
                selectedAemVersion?.apply {
                  val suggestedHtlVersion = HtlVersion.getFirstCompatibleWith(selectedAemVersion)
                  newHtlVersion.set(suggestedHtlVersion.version)
                }
              }
            }
      }
      row("HTL version:") {
        htlVersionComboBox = comboBox(HtlVersion.versions())
            .enabledIf(isSetHtlVersionManuallyCheckbox.selected)
            .bindItem(newHtlVersion)
      }
    }
  }

  fun getPanelState(): AemProjectSettings {
    val newState = AemProjectSettings()
    newState.aemVersion = AemVersion.fromFullVersion(newAemVersion.get()) ?: currentState.aemVersion
    newState.htlVersion = HtlVersion.fromVersion(newHtlVersion.get()) ?: currentState.htlVersion
    newState.isManuallyDefinedHtlVersion = isManuallyDefinedHtlVersion.get()
    return newState
  }
}
