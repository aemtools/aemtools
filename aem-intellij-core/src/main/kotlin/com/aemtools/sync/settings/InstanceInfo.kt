package com.aemtools.sync.settings

import com.aemtools.sync.util.SyncConstants
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

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as InstanceInfo

    if (enabled != other.enabled) return false
    if (url != other.url) return false
    if (login != other.login) return false
    if (password != other.password) return false

    return true
  }

  override fun hashCode(): Int {
    var result = enabled?.hashCode() ?: 0
    result = 31 * result + (url?.hashCode() ?: 0)
    result = 31 * result + (login?.hashCode() ?: 0)
    result = 31 * result + (password?.hashCode() ?: 0)
    return result
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
