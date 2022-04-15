package com.aemtools.integration.sync.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.AbstractCollection
import com.intellij.util.xmlb.annotations.Tag
import org.aemsync.core.api.model.AemInstance

/**
 * @author Dmytro Liakhov
 */
@State(
    name = "AemToolsProjectConfiguration",
    storages = [(Storage(StoragePathMacros.WORKSPACE_FILE))]
)
class AemToolsProjectConfiguration : PersistentStateComponent<AemToolsProjectConfiguration> {

  @Tag("instances")
  @AbstractCollection
  val instances: MutableList<AemInstance> = ArrayList()

  @Tag
  var isSyncEnabled: Boolean = false

  override fun getState(): AemToolsProjectConfiguration? = this

  /**
   * Add an AEM instance to the storage.
   *
   * @param instance the instance to add
   */
  fun addInstance(instance: AemInstance) {
    instances.add(instance)
  }

  /**
   * Remove an AEM instance from the storage.
   *
   * @param instance the instance to remove
   */
  fun removeInstance(instance: AemInstance) {
    instances.remove(instance)
  }

  override fun loadState(state: AemToolsProjectConfiguration) {
    instances.addAll(state.instances)
    this.isSyncEnabled = state.isSyncEnabled
  }

  companion object {

    /**
     * Get the instance of [AemToolsProjectConfiguration] associated
     * with given [Project].
     *
     * @param project the project
     */
    fun getInstance(project: Project): AemToolsProjectConfiguration {
      val instanceInfo = ServiceManager.getService(
          project,
          AemToolsProjectConfiguration::class.java)
      if (instanceInfo.instances.isEmpty()) {
        with(instanceInfo.instances) {
          add(AemInstance(
              "author",
              "http://localhost:4502",
              "default"
          ))
          add(AemInstance(
              "publish",
              "http://localhost:4503",
              "default"
          ))
        }
      }
      return instanceInfo
    }

  }

}
