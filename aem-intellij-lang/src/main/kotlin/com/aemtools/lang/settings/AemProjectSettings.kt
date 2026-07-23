package com.aemtools.lang.settings

import com.aemtools.lang.settings.model.AemVersion
import com.aemtools.lang.settings.model.HtlVersion
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project

/**
 * Storage for AEM project settings.
 *
 * @author Kostiantyn Diachenko
 */
@State(
    name = "AemProjectConfiguration",
    storages = [(Storage(StoragePathMacros.WORKSPACE_FILE))]
)
class AemProjectSettings : PersistentStateComponent<AemProjectSettings.State> {

  data class State(
      var aemVersion: AemVersion = AemVersion.latest(),
      var htlVersion: HtlVersion = HtlVersion.latest(),
      var isManuallyDefinedHtlVersion: Boolean = false,
      var wasInitialized: Boolean = false
  )

  private var state = State()

  var aemVersion: AemVersion
    get() = state.aemVersion
    set(value) {
      state.aemVersion = value
    }

  var htlVersion: HtlVersion
    get() = state.htlVersion
    set(value) {
      state.htlVersion = value
    }

  var isManuallyDefinedHtlVersion: Boolean
    get() = state.isManuallyDefinedHtlVersion
    set(value) {
      state.isManuallyDefinedHtlVersion = value
    }

  override fun getState(): State = state

  override fun loadState(state: State) {
    this.state = state.copy(wasInitialized = true)
  }

  fun updateFrom(newState: State) {
    state = newState.copy(wasInitialized = true)
  }

  fun isInitialized(): Boolean = state.wasInitialized

  companion object {

    /**
     * Get instance of [AemProjectSettings] associated with given [Project].
     *
     * @param project the project
     * @return htl root directories instance, may be *null*
     */
    fun getInstance(project: Project): AemProjectSettings =
        project.getService(AemProjectSettings::class.java)

  }
}
