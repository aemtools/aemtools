package com.aemtools.service.repository.inmemory.util

import com.aemtools.completion.model.JsTypeInfo
import com.aemtools.completion.model.editconfig.XmlAttributeDefinition
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

/**
 * @author Dmytro_Troynikov
 */
class XmlAttributeDefinitionDeserializer : JsonDeserializer<XmlAttributeDefinition> {

  override fun deserialize(json: JsonElement?, typeOfT: Type?,
                           context: JsonDeserializationContext?): XmlAttributeDefinition? {

    val jsonObject = json as JsonObject

    return XmlAttributeDefinition(
        jsonObject.get("name").asString,
        extractTypeInfo(jsonObject),
        extractValues(jsonObject).orEmpty(),
        jsonObject.get("delimiter")?.asString.orEmpty()
    )
  }

  private fun extractValues(jsonObject: JsonObject): List<String> {
    return jsonObject.get("values")?.asJsonArray?.map { it.asString } ?: listOf()
  }

  private fun extractTypeInfo(jsonObject: JsonObject): JsTypeInfo {
    val rawString = jsonObject.get("type")?.asString ?: ""
    return JsTypeInfo(rawString.split("|"))
  }

}
