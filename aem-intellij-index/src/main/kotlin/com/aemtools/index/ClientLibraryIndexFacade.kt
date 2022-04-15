package com.aemtools.index

import com.aemtools.common.util.allFromFbi
import com.aemtools.index.model.ClientlibraryModel
import com.intellij.openapi.project.Project

/**
 * @author Arsen Retiznyk
 */
object ClientLibraryIndexFacade {

  /**
   * Collect all [ClientlibraryModel] objects available in project.
   *
   * @param project the project
   * @return list of [ClientlibraryModel] objects
   */
  fun getAllClientLibraryModels(project: Project): List<ClientlibraryModel> =
      allFromFbi(ClientlibraryIndex.CLIENTLIBRARY_ID, project)

  /**
   * Find all clientlibraries that contain given category.
   *
   * @param project the project
   * @param category the category
   * @return list of [ClientlibraryModel] objects that have given category
   */
  fun findClientlibsByCategory(project: Project, category: String): List<ClientlibraryModel> =
    getAllClientLibraryModels(project).filter { it.categories.contains(category) }

}
