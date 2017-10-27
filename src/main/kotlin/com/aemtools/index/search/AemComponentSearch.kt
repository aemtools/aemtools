package com.aemtools.index.search

import com.aemtools.index.AemComponentClassicDialogIndex
import com.aemtools.index.AemComponentDeclarationIndex
import com.aemtools.index.AemComponentTouchUIDialogIndex
import com.aemtools.index.model.AemComponentDefinition
import com.aemtools.index.model.dialog.AemComponentClassicDialogDefinition
import com.aemtools.index.model.dialog.AemComponentTouchUIDialogDefinition
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
   * Resource type string may be of full path e.g.
   *
   * `/app/myapp/components/component`
   *
   * or of "not full" path e.g.
   *
   * `myapp/components/component`
   *
   * or of "project relative" path e.g.
   *
   * `components/component`
   *
   * @param resourceType the resource type string
   * @param project the project
   * @return component definition or *null* if no component was found
   */
  fun findByResourceType(resourceType: String, project: Project): AemComponentDefinition?
      = allComponentDeclarations(project).find(
      resourceType.let { typeToFind ->
        if (typeToFind.startsWith("/apps/")) {
          { definition: AemComponentDefinition ->
            definition.resourceType() == typeToFind
          }
        } else {
          { definition: AemComponentDefinition ->
            definition.resourceType().let {
              it.substringAfter("/apps/") == typeToFind
                  || it.substringAfter("/apps/")
                  .substringAfter("/") == typeToFind
            }
          }
        }
      }
  )

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
