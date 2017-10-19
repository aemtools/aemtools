package com.aemtools.service.repository.impl

import com.aemtools.service.repository.IRepositoryService
import com.aemtools.service.repository.WidgetDocRepository
import com.aemtools.service.repository.inmemory.FileDocRepository

/**
 * @author Dmytro_Troynikov.
 */
class RepositoryService : IRepositoryService {

  private val widgetRepository = FileDocRepository

  override fun getWidgetDocRepository(): WidgetDocRepository {
    return widgetRepository
  }

}
