package com.aemtools.index

import com.aemtools.common.util.allFromFbi
import com.aemtools.index.model.ClientlibraryModel
import com.intellij.openapi.project.Project

object ClientLibraryIndexFacade {

  /**
   * Collect all [ClientlibraryModel] objects available in project.
   *
   * @param project the project
   * @return list of [ClientlibraryModel] objects
   */
  fun getAllClientLibraryModels(project: Project): List<ClientlibraryModel> =
      allFromFbi(ClientlibraryIndex.CLIENTLIBRARY_ID, project)
}
