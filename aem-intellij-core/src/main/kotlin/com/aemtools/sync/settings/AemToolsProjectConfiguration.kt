package com.aemtools.sync.settings

import com.aemtools.sync.settings.model.AemInstance
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.AbstractCollection
import com.intellij.util.xmlb.annotations.Tag

/**
 * @author Dmytro Liakhov
 */
@State(
    name = "AemToolsProjectConfiguration",
    storages = arrayOf(
        Storage(StoragePathMacros.WORKSPACE_FILE))
)
class AemToolsProjectConfiguration : PersistentStateComponent<AemToolsProjectConfiguration> {

  @Tag("instances")
  @AbstractCollection
  val instances: MutableList<AemInstance> = ArrayList()

  @Tag
  var isSyncEnabled: Boolean = false

  override fun getState(): AemToolsProjectConfiguration? = this

  fun addInstance(instance: AemInstance) {
    instances.add(instance)
  }

  fun removeInstance(instance: AemInstance) {
    instances.remove(instance)
  }

  override fun loadState(state: AemToolsProjectConfiguration?) {
    state?.let {
      instances.addAll(state.instances)
      this.isSyncEnabled = state.isSyncEnabled
    }
  }

  companion object {

    fun getInstance(project: Project): AemToolsProjectConfiguration {
      val instanceInfo = ServiceManager.getService(project, AemToolsProjectConfiguration::class.java)
      if (instanceInfo.instances.isEmpty()) {
        instanceInfo.instances.add(AemInstance.default)
      }
      return instanceInfo
    }

  }

}
