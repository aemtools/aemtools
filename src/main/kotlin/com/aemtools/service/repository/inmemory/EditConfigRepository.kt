package com.aemtools.service.repository.inmemory

import com.aemtools.completion.model.editconfig.XmlTagDefinition
import com.aemtools.service.repository.const
import com.aemtools.service.repository.inmemory.util.XmlTagDefinitionDeserializer
import com.aemtools.service.repository.inmemory.util.readJson
import com.google.gson.GsonBuilder

/**
 * @author Dmytro_Troynikov
 */
object EditConfigRepository {

  private val attributesData: List<XmlTagDefinition> = readJson(
      const.file.CQ_EDIT_CONFIG,
      GsonBuilder().registerTypeAdapter(XmlTagDefinition::class.java, XmlTagDefinitionDeserializer())
          .create()
  )

  /**
   * Get tag definition by name.
   *
   * @param name the name
   * @return xml tag definition, may be empty
   */
  fun getTagDefinitionByName(name: String): XmlTagDefinition =
      attributesData.find { it.name == name } ?: XmlTagDefinition.empty()

}
