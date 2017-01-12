package com.aemtools.service.repository

import com.aemtools.completion.model.WidgetDoc

/**
 * @author Dmytro_Troynikov.
 */
interface WidgetDocRepository {

    fun findByXType(xtype : String) : WidgetDoc?

    fun findByClass(className : String) : WidgetDoc?

    fun findXTypes(query : String?) : List<String>

}