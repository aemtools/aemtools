package com.aemtools.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros.WORKSPACE_FILE
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.AbstractCollection
import com.intellij.util.xmlb.annotations.Tag

/**
 * Storage for HTL root folders.
 *
 * @author Dmytro Troynikov
 */
@State(
    name = "HtlRootsConfiguration",
    storages = arrayOf(
        Storage(WORKSPACE_FILE)
    )
)
class HtlRootDirectories : PersistentStateComponent<HtlRootDirectories> {

  @Tag("htl-roots")
  @AbstractCollection(surroundWithTag = true)
  val directories: MutableList<String> = ArrayList()

  /**
   * Add folder as Htl root.
   *
   * @param folder folder to add to roots
   */
  fun addRoot(folder: String) {
    directories.add(folder)
  }

  /**
   * Remove folder from Htl roots.
   *
   * @param folder folder to remove
   */
  fun removeRoot(folder: String) {
    directories.remove(folder)
  }

  override fun loadState(state: HtlRootDirectories?) {
    directories.clear()
    state?.let {
      directories.addAll(state.directories)
    }
  }

  override fun getState(): HtlRootDirectories? = this

  companion object {

    /**
     * Get instance of [HtlRootDirectories] associated with given [Project].
     *
     * @param project the project
     * @return htl root directories instance, may be *null*
     */
    fun getInstance(project: Project): HtlRootDirectories? =
        ServiceManager.getService(project, HtlRootDirectories::class.java)

  }

}
