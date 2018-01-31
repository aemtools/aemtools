package com.aemtools.sync.settings

import com.aemtools.sync.util.SyncConstants
import com.intellij.openapi.components.*
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
  var enabled: Boolean? = null

  @Tag
  var url: String? = null

  @Tag
  var login: String? = null

  @Tag
  var password: String? = null

  override fun getState(): InstanceInfo? = this

  override fun loadState(state: InstanceInfo?) {
    state?.let {
      setInstanceInfo(state)
    }
  }

  private fun isEmpty(): Boolean {
    return (enabled == null || enabled == false) && url.isNullOrBlank()
        && login.isNullOrBlank() && password.isNullOrBlank()
  }

  private fun setInstanceInfo(state: InstanceInfo) {
    enabled = state.enabled
    url = state.url
    login = state.login
    password = state.password
  }

  private fun setDefaultInstanceInfo() {
    enabled = SyncConstants.DEFAULT_IS_ENABLED_SYNC
    url = SyncConstants.DEFAULT_URL_INSTANCE
    login = SyncConstants.DEFAULT_LOGIN
    password = SyncConstants.DEFAULT_PASSWORD
  }

  companion object {

    fun getInstance(project: Project): InstanceInfo {
      val instanceInfo = ServiceManager.getService(project, InstanceInfo::class.java)
      if (instanceInfo.isEmpty()) {
        instanceInfo.setDefaultInstanceInfo()
      }
      return instanceInfo
    }

  }

}
