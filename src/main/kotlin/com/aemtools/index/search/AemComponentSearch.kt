package com.aemtools.index.search

import com.aemtools.index.AemComponentDeclarationIndex
import com.aemtools.index.model.AemComponentDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

/**
 * Search functionality related to aem components.
 *
 * @author Dmytro Troynikov
 */
object AemComponentSearch {

    /**
     * Find all aem component declarations present in the project.
     *
     * @param project the project
     * @return list of aem component declarations
      */
    fun allComponentDeclarations(project: Project) : List<AemComponentDefinition> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(AemComponentDeclarationIndex.AEM_COMPONENT_DECLARATION_INDEX_ID, project)

        return keys.flatMap {
            fbi.getValues(
                    AemComponentDeclarationIndex.AEM_COMPONENT_DECLARATION_INDEX_ID,
                    it,
                    GlobalSearchScope.projectScope(project))
        }
    }

}