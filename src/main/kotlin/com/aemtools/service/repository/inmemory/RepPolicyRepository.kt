package com.aemtools.service.repository.inmemory

import com.aemtools.completion.model.editconfig.XmlTagDefinition
import com.aemtools.service.repository.const
import com.aemtools.service.repository.inmemory.util.FileUtils
import com.aemtools.service.repository.inmemory.util.XmlTagDefinitionDeserializer
import com.google.gson.GsonBuilder

/**
 * @author Dmytro Troynikov.
 */
object RepPolicyRepository {

  private val data: MutableList<XmlTagDefinition> = arrayListOf()

  init {
    loadData()
  }

  private fun loadData() {
    val jsonString = FileUtils.readFileAsString(const.file.REP_POLICY)

    val gson = GsonBuilder()
        .registerTypeAdapter(XmlTagDefinition::class.java, XmlTagDefinitionDeserializer())
        .create()

    val result: Array<XmlTagDefinition> =
        gson.fromJson(jsonString, emptyArray<XmlTagDefinition>().javaClass)

    data.addAll(result)
  }

  /**
   * Get tag definition by name.
   *
   * @param name the name
   * @return xml tag definition, may be empty
   */
  fun getTagDefinitionByName(name: String): XmlTagDefinition {
    return data.find { it.name == name } ?: XmlTagDefinition.empty()
  }
}
