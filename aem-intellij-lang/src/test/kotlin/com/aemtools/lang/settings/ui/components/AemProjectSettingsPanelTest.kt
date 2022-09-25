package com.aemtools.lang.settings.ui.components

import com.aemtools.lang.settings.AemProjectSettings
import com.aemtools.lang.settings.model.AemVersion
import com.aemtools.lang.settings.model.HtlVersion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for [AemProjectSettingsPanel].
 *
 * @author Kostiantyn Diachenko
 */
class AemProjectSettingsPanelTest {

  @Test
  fun `should save custom HTL version`() {
    val panel = initAemProjectSettingsPanel()

    panel.htlVersionsModel.selectedItem = HtlVersion.V_1_3.version
    panel.isSetHtlVersionManuallyCheckbox.isSelected = true

    val panelState = panel.getPanelState()

    assertEquals(AemVersion.V_6_5, panelState.aemVersion)
    assertEquals(HtlVersion.V_1_3, panelState.htlVersion)
    assertTrue(panelState.isManuallyDefinedHtlVersion)
  }

  @Test
  fun `should save HTL version from current state`() {
    val panel = initAemProjectSettingsPanel()

    val panelState = panel.getPanelState()

    assertEquals(AemVersion.V_6_5, panelState.aemVersion)
    assertEquals(HtlVersion.V_1_4, panelState.htlVersion)
    assertFalse(panelState.isManuallyDefinedHtlVersion)
  }

  @Test
  fun `should save custom AEM version`() {
    val panel = initAemProjectSettingsPanel()

    panel.aemVersionsModel.selectedItem = AemVersion.V_6_4.version

    val panelState = panel.getPanelState()

    assertEquals(AemVersion.V_6_4, panelState.aemVersion)
    assertEquals(HtlVersion.V_1_4, panelState.htlVersion)
    assertFalse(panelState.isManuallyDefinedHtlVersion)
  }

  @Test
  fun `should save AEM version from current state`() {
    val panel = initAemProjectSettingsPanel()

    val panelState = panel.getPanelState()

    assertEquals(AemVersion.V_6_5, panelState.aemVersion)
    assertEquals(HtlVersion.V_1_4, panelState.htlVersion)
    assertFalse(panelState.isManuallyDefinedHtlVersion)
  }

  private fun initAemProjectSettingsPanel(): AemProjectSettingsPanel {
    val currentState = AemProjectSettings()
    val panel = AemProjectSettingsPanel(currentState)
    panel.getPanel()
    return panel
  }
}
