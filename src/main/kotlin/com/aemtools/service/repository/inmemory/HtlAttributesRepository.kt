package com.aemtools.service.repository.inmemory

import com.aemtools.completion.model.htl.ContextObject
import com.aemtools.completion.model.htl.HtlAttributeMetaInfo
import com.aemtools.completion.model.htl.HtlOption
import com.aemtools.service.repository.const
import com.aemtools.service.repository.inmemory.util.readJson

/**
 * @author Dmytro_Troynikov
 */
object HtlAttributesRepository {

    data class HtlContextValue(val name: String,
                               val description: String,
                               val additionalDescription: String?)

    private val attributesData: List<HtlAttributeMetaInfo> = readJson(const.file.SIGHTLY_ATTRIBUTES_DOCUMENTATION)
    private val contextObjects: List<ContextObject> = readJson(const.file.CONTEXT_OBJECTS)
    private val htlOptions: List<HtlOption> = readJson(const.file.HTL_OPTIONS)
    private val htlContextValues: List<HtlContextValue> = readJson(const.file.HTL_CONTEXT_VALUES)

    fun getAttributesData(): List<HtlAttributeMetaInfo> = attributesData

    fun getContextObjects(): List<ContextObject> = contextObjects

    fun getHtlOptions(): List<HtlOption> = htlOptions

    fun getContextValues(): List<HtlContextValue> = htlContextValues

    fun findContextObject(name: String): ContextObject? =
            contextObjects.find { it.name == name }

}
