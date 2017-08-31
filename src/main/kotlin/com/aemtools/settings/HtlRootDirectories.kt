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
@State(name = "HtlRootsConfiguration", storages = arrayOf(Storage("htl-roots.xml")))
class HtlRootDirectories : PersistentStateComponent<HtlRootDirectories> {
    val directories: MutableList<String> = ArrayList()

    fun addRoot(folder: String) {
        directories.add(folder)
    }

    override fun loadState(state: HtlRootDirectories?) {
        directories.clear()
        state?.let {
            directories.addAll(state.directories)
        }
    }

    override fun getState(): HtlRootDirectories? {
        return this
    }

    companion object {

        fun getInstance(): HtlRootDirectories? =
                ServiceManager.getService(HtlRootDirectories::class.java)

    }

}