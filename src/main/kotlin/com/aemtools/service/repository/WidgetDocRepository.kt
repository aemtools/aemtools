package com.aemtools.service.repository

import com.aemtools.completion.model.WidgetDoc

/**
 * @author Dmytro_Troynikov.
 */
interface WidgetDocRepository {

    /**
     * Find [WidgetDoc] object by xtype.
     *
     * @param xtype the xtype
     * @return widget doc, *null* if no widget doc found
     * by given xtype
     */
    fun findByXType(xtype : String) : WidgetDoc?

    /**
     * Find [WidgetDoc] object by class name.
     *
     * @param className the class name
     * @return widget doc, *null* if no widget doc found
     * by given class name
     */
    fun findByClass(className : String) : WidgetDoc?


    fun findXTypes(query : String?) : List<String>

}
