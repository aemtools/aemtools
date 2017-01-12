package com.aemtools.service.repository.inmemory

import com.aemtools.completion.model.editconfig.XmlTagDefinition
import com.aemtools.service.repository.const
import com.aemtools.service.repository.inmemory.util.FileUtils
import com.aemtools.service.repository.inmemory.util.XmlTagDefinitionDeserializer
import com.google.gson.GsonBuilder

/**
 * @author Dmytro_Troynikov
 */
object EditConfigRepository {

    private val attributesData: MutableList<XmlTagDefinition> = arrayListOf()

    init {
        loadData()
    }

    private fun loadData() {
        val jsonString = FileUtils.readFileAsString(const.file.CQ_EDIT_CONFIG)

        val gson = GsonBuilder()
                .registerTypeAdapter(XmlTagDefinition::class.java, XmlTagDefinitionDeserializer())
                .create()

        val result: Array<XmlTagDefinition> =
                gson.fromJson(jsonString, emptyArray<XmlTagDefinition>().javaClass)
        attributesData.addAll(result)
    }

    fun getAttributesData(): List<XmlTagDefinition> = attributesData

    fun getTagDefinitionByName(name: String): XmlTagDefinition {
        return attributesData.find { it.name == name } ?: XmlTagDefinition.empty()
    }

}