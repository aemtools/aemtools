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

    data class HtlOption(val name: String,
                         val type: String,
                         val description: String,
                         val examples: List<String>,
                         val link: String)

    data class HtlContextValue(val name: String,
                               val description: String,
                               val additionalDescription: String?)

    private val attributesData: List<HtlAttributeMetaInfo> = readJson(const.file.SIGHTLY_ATTRIBUTES_DOCUMENTATION)
    private val contextObjects: List<ContextObject> = readJson(const.file.CONTEXT_OBJECTS)
    private val htlOptions: List<HtlOption> = readJson(const.file.HTL_OPTIONS)
    private val htlContextValues: List<HtlContextValue> = readJson(const.file.HTL_CONTEXT_VALUES)

    private inline fun <reified T> readJson(file: String): List<T> {
        val jsonString = FileUtils.readFileAsString(file)
        return Gson().fromJson(jsonString, emptyArray<T>().javaClass).toList()
    }

    fun getAttributesData(): List<HtlAttributeMetaInfo> = attributesData

    fun getContextObjects(): List<ContextObject> = contextObjects

    fun getHtlOptions(): List<HtlOption> = htlOptions

    fun getContextValues(): List<HtlContextValue> = htlContextValues

    fun  findContextObject(name: String): ContextObject? {
        return contextObjects.find {it.name == name}
    }
}
