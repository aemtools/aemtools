package com.aemtools.service

import com.aemtools.service.repository.IRepositoryService
import com.aemtools.service.repository.WidgetDocRepository
import com.aemtools.service.repository.inmemory.EditConfigRepository
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.aemtools.service.repository.inmemory.RepPolicyRepository
import com.intellij.openapi.components.ServiceManager

/**
 * @author Dmytro_Troynikov
 */
object ServiceFacade {

    /**
     * Get {@link WidgetDocRepository} instance
     */
    fun getWidgetRepository(): WidgetDocRepository {
        return ServiceManager
                .getService(IRepositoryService::class.java)
                .getWidgetDocRepository()
    }

    fun getHtlAttributesRepository(): HtlAttributesRepository
            = HtlAttributesRepository

    fun getEditConfigRepository() = EditConfigRepository

    fun getRepPolicyRepository() = RepPolicyRepository

}
