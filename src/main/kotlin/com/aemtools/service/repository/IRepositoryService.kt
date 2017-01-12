package com.aemtools.service.repository

import com.aemtools.service.repository.inmemory.HtlAttributesRepository

/**
 * @author Dmytro_Troynikov.
 */
interface IRepositoryService {

    fun getWidgetDocRepository(): WidgetDocRepository

}