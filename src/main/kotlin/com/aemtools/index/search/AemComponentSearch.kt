package com.aemtools.index.search

import com.aemtools.index.AemComponentClassicDialogIndex
import com.aemtools.index.AemComponentDeclarationIndex
import com.aemtools.index.AemComponentTouchUIDialogIndex
import com.aemtools.index.model.AemComponentClassicDialogDefinition
import com.aemtools.index.model.AemComponentDefinition
import com.aemtools.index.model.AemComponentTouchUIDialogDefinition
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
    fun allComponentDeclarations(project: Project): List<AemComponentDefinition> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(AemComponentDeclarationIndex.AEM_COMPONENT_DECLARATION_INDEX_ID, project)

        return keys.flatMap {
            fbi.getValues(
                    AemComponentDeclarationIndex.AEM_COMPONENT_DECLARATION_INDEX_ID,
                    it,
                    GlobalSearchScope.projectScope(project))
        }.filterNotNull()
    }

    /**
     * Find component by resource type.
     * @param resourceType the resource type string
     * @param project the project
     * @return component definition or *null* if no component was found
     */
    fun findByResourceType(resourceType: String, project: Project): AemComponentDefinition?
            = allComponentDeclarations(project).find { it.resourceType() == resourceType }

    /**
     * Find classic dialog definition by resource type.
     *
     * @param resourceType resource type string
     * @param project the project
     * @return classic dialog for given resource type, *null* if no dialog was found
     */
    fun findClassicDialogByResourceType(resourceType: String, project: Project): AemComponentClassicDialogDefinition?
            = FileBasedIndex.getInstance()
            .getValues(
                    AemComponentClassicDialogIndex.AEM_COMPONENT_CLASSIC_DIALOG_INDEX_ID,
                    resourceType,
                    GlobalSearchScope.projectScope(project))
            .firstOrNull()

    /**
     * Find touch ui dialog definition by component's resource type.
     *
     * @param resourceType resource type string
     * @param project the project
     * @return touch ui dialog for given resource type, *null* if no dialog was found
     */
    fun findTouchUIDialogByResourceType(resourceType: String, project: Project): AemComponentTouchUIDialogDefinition?
            = FileBasedIndex.getInstance()
            .getValues(
                    AemComponentTouchUIDialogIndex.AEM_COMPONENT_TOUCH_UI_DIALOG_INDEX,
                    resourceType,
                    GlobalSearchScope.projectScope(project))
            .firstOrNull()

}