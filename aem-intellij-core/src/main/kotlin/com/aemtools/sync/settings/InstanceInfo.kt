package com.aemtools.sync.settings

import com.aemtools.sync.settings.model.InstanceInfoModel
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Tag

/**
 * @author Dmytro Liakhov
 */
@State(
    name = "InstanceInfo",
    storages = arrayOf(
        Storage(StoragePathMacros.WORKSPACE_FILE))
)
class InstanceInfo : PersistentStateComponent<InstanceInfo> {

  @Tag
  private var enabled: Boolean? = null

  @Tag
  private var url: String? = null

  @Tag
  private var login: String? = null

  @Tag
  private var password: String? = null

  var instanceInfoModel: InstanceInfoModel? = null

  override fun getState(): InstanceInfo? = this

  override fun loadState(state: InstanceInfo?) {
    state?.let {
      instanceInfoModel = InstanceInfoModel(state.enabled, state.url, state.login, state.password)
    }
  }

  companion object {

    fun getInstance(project: Project): InstanceInfo = ServiceManager.getService(project, InstanceInfo::class.java)

  }

}
