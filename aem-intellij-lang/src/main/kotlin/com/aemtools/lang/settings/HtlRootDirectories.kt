package com.aemtools.lang.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros.WORKSPACE_FILE
import com.intellij.openapi.project.Project

/**
 * Storage for HTL root folders.
 *
 * @author Dmytro Primshyts
 */
@State(
    name = "HtlRootsConfiguration",
    storages = [(Storage(WORKSPACE_FILE))]
)
class HtlRootDirectories : PersistentStateComponent<HtlRootDirectories.State> {

  data class State(var directories: MutableList<String> = ArrayList())

  private var state = State()

  val directories: MutableList<String>
    get() = state.directories

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

  override fun loadState(state: State) {
    this.state = state
  }

  override fun getState(): State = state

  companion object {

    /**
     * Get instance of [HtlRootDirectories] associated with given [Project].
     *
     * @param project the project
     * @return htl root directories instance, may be *null*
     */
    fun getInstance(project: Project): HtlRootDirectories? =
        project.getService(HtlRootDirectories::class.java)

  }

}
