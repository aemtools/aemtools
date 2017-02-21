package com.aemtools.service.repository.inmemory

import com.aemtools.completion.model.htl.ContextObject
import com.aemtools.completion.model.htl.HtlAttributeMetaInfo
import com.aemtools.service.repository.const
import com.aemtools.service.repository.inmemory.util.FileUtils
import com.google.gson.Gson

/**
 * @author Dmytro_Troynikov
 */
object HtlAttributesRepository {

    private val attributesData: MutableList<HtlAttributeMetaInfo> = arrayListOf()
    private val contextObjects: MutableList<ContextObject> = arrayListOf()
    init {
        loadAttributesData()
        loadContextObjects()
    }
    private fun loadAttributesData() {
        val jsonString = FileUtils.readFileAsString(const.file.SIGHTLY_ATTRIBUTES_DOCUMENTATION)

        val attributes : Array<HtlAttributeMetaInfo> =
                Gson().fromJson(jsonString, emptyArray<HtlAttributeMetaInfo>().javaClass)
        attributesData.addAll(attributes)
    }

    private fun loadContextObjects() {
        val jsonString = FileUtils.readFileAsString(const.file.CONTEXT_OBJECTS)
        val result : Array<ContextObject> =
                Gson().fromJson(jsonString, emptyArray<ContextObject>().javaClass)
        contextObjects.addAll(result)
    }

    fun getAttributesData(): List<HtlAttributeMetaInfo> = attributesData.toList()

    fun getContextObjects(): List<ContextObject> = contextObjects.toList()

    fun  findContextObject(name: String): ContextObject? {
        return contextObjects.find {it.name == name}
    }
}
