package com.aemtools.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * Storage for HTL root folders.
 *
 * @author Dmytro Troynikov
 */
@State(
        name = "HtlRootsConfiguration",
        storages = arrayOf(Storage("htl-roots.xml"))
)
class HtlRootDirectories : PersistentStateComponent<HtlRootDirectories> {

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

        fun getInstance(): HtlRootDirectories? =
                ServiceManager.getService(HtlRootDirectories::class.java)

    }

}