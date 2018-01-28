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
        name = "InstanceInfoService",
        storages = arrayOf(
                Storage(StoragePathMacros.WORKSPACE_FILE))
)
class InstanceInfoService : PersistentStateComponent<InstanceInfoModel> {

  @Tag(value = "instance-info")
  var instanceInfoModel: InstanceInfoModel? = null

  override fun getState(): InstanceInfoModel? = instanceInfoModel

  override fun loadState(state: InstanceInfoModel?) {
    instanceInfoModel = state
  }

  companion object {

    fun getInstance(project: Project) = ServiceManager.getService(project, InstanceInfoService::class.java)

  }

}