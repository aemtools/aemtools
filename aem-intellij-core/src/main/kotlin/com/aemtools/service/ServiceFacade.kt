package com.aemtools.service

import com.aemtools.service.repository.IRepositoryService
import com.aemtools.service.repository.WidgetDocRepository
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.aemtools.service.repository.inmemory.RepPolicyRepository
import com.intellij.openapi.components.ServiceManager

/**
 * @author Dmytro Primshyts
 */
object ServiceFacade {

  /**
   * Get [WidgetDocRepository] instance.
   *
   * @return instance of widget doc repository
   */
  fun getWidgetRepository(): WidgetDocRepository = ServiceManager
      .getService(IRepositoryService::class.java)
      .getWidgetDocRepository()

  /**
   * Get [HtlAttributesRepository] instance.
   *
   * @return instance of htl attribute repository
   */
  fun getHtlAttributesRepository(): HtlAttributesRepository = HtlAttributesRepository

  /**
   * Get [RepPolicyRepository] instance.
   *
   * @return instance rep policy repository
   */
  fun getRepPolicyRepository() = RepPolicyRepository

}
