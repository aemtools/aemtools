package com.aemtools.lang.settings

import com.aemtools.lang.settings.model.AemVersion
import com.aemtools.lang.settings.model.HtlVersion
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Tag

/**
 * Storage for AEM project settings.
 *
 * @author Kostiantyn Diachenko
 */
@State(
    name = "AemProjectConfiguration",
    storages = [(Storage(StoragePathMacros.WORKSPACE_FILE))]
)
class AemProjectSettings : PersistentStateComponent<AemProjectSettings> {

  @Tag
  var aemVersion: AemVersion = AemVersion.latest()

  @Tag
  var htlVersion: HtlVersion = HtlVersion.latest()

  @Tag
  var isManuallyDefinedHtlVersion: Boolean = false

  var wasInitialized: Boolean = false

  override fun getState(): AemProjectSettings = this

  override fun loadState(state: AemProjectSettings) {
    aemVersion = state.aemVersion
    htlVersion = state.htlVersion
    isManuallyDefinedHtlVersion = state.isManuallyDefinedHtlVersion
    wasInitialized = true
  }

  fun isInitialized(): Boolean = wasInitialized

  companion object {

    /**
     * Get instance of [AemProjectSettings] associated with given [Project].
     *
     * @param project the project
     * @return htl root directories instance, may be *null*
     */
    fun getInstance(project: Project): AemProjectSettings =
        project.getService(AemProjectSettings::class.java)

    fun clone(aemProjectSettings: AemProjectSettings): AemProjectSettings {
      val newAemProjectSettings = AemProjectSettings()
      newAemProjectSettings.loadState(aemProjectSettings)
      return newAemProjectSettings
    }

  }
}
