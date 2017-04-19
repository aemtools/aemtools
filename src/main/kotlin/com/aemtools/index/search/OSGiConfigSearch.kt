package com.aemtools.index.search

import com.aemtools.index.OSGiConfigIndex
import com.aemtools.index.model.OSGiConfiguration
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

/**
 * @author Dmytro_Troynikov
 */
object OSGiConfigSearch {

    fun findConfigsForClass(fqn: String, project: Project): List<OSGiConfiguration> {
        val configs = getAllConfigs(project)

        return configs.filter { it.fullQualifiedName == fqn }
    }

    fun getAllConfigs(project: Project): List<OSGiConfiguration> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(OSGiConfigIndex.OSGI_INDEX_ID, project)

        val values = keys.flatMap {
            fbi.getValues(OSGiConfigIndex.OSGI_INDEX_ID, it, GlobalSearchScope.allScope(project))
        }.filterNotNull()

        return values
    }

}