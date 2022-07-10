package com.aemtools.lang.settings.components

import com.aemtools.lang.settings.AemProjectSettings
import com.aemtools.lang.settings.model.AemVersion
import com.aemtools.lang.settings.model.HtlVersion
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.selected

class AemProjectSettingsPanel(private val currentState: AemProjectSettings) {

  private val htlVersionsModel = CollectionComboBoxModel(HtlVersion.versions(), currentState.htlVersion)
  private val aemVersionsModel = buildAemComboboxModel(currentState)

  private lateinit var isSetHtlVersionManuallyCheckbox: Cell<JBCheckBox>
  private lateinit var htlVersionComboBox: Cell<ComboBox<String>>

  fun getPanel(): DialogPanel = panel {
    row("AEM Version:") {
      comboBox(aemVersionsModel)
          .comment("Select AEM version on the current project")
    }
    group("HTL Configuration") {
      row {
        isSetHtlVersionManuallyCheckbox = checkBox("Set HTL versions manually")
            .gap(RightGap.SMALL)
            .comment("""
            By default, it is set automatically based on AEM version.
            An implementation of version 1.4 of the HTL is available in AEM 6.3 SP3 and AEM 6.4 SP1.
          """.trimIndent())
            .actionListener { _, component ->
              if (!component.selected()) {
                htlVersionsModel.selectedItem = currentState.htlVersion
              }
            }
      }
      row("HTL version:") {
        htlVersionComboBox = comboBox(htlVersionsModel)
            .enabledIf(isSetHtlVersionManuallyCheckbox.selected)
      }
    }
  }

  fun getPanelState(): AemProjectSettings {
    val newState = AemProjectSettings()
    newState.aemVersion = aemVersionsModel.selected ?: currentState.aemVersion
    if (isSetHtlVersionManuallyCheckbox.selected()) {
      newState.htlVersion = htlVersionsModel.selected ?: currentState.htlVersion
    } else {
      newState.htlVersion = currentState.htlVersion
    }
    return newState
  }

  private fun buildAemComboboxModel(
      currentState: AemProjectSettings
  ): CollectionComboBoxModel<String> {
    return AemVersionComboBoxModel(AemVersion.versions(), currentState.aemVersion) {
      if (isSetHtlVersionManuallyCheckbox.selected()) {
        return@AemVersionComboBoxModel
      }
      val aemVersion = AemVersion.fromVersion(it)
      val suggestedHtlVersion = HtlVersion.getFirstCompatibleWith(aemVersion)
      htlVersionsModel.selectedItem = suggestedHtlVersion.version
    }
  }
}
