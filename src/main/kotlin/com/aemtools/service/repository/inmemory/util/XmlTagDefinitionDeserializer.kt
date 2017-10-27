package com.aemtools.service.repository.inmemory.util

import com.aemtools.completion.model.editconfig.XmlAttributeDefinition
import com.aemtools.completion.model.editconfig.XmlTagDefinition
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

/**
 * @author Dmytro_Troynikov
 */
class XmlTagDefinitionDeserializer : JsonDeserializer<XmlTagDefinition> {
  override fun deserialize(json: JsonElement?, typeOfT: Type?,
                           context: JsonDeserializationContext?): XmlTagDefinition? {

    val jsonObject = json as JsonObject

    return XmlTagDefinition(
        jsonObject.get("nodeName")?.asString ?: "",
        jsonObject.get("childNodes")?.asJsonArray?.map { it.asString } ?: listOf(),
        jsonObject.get("attributes")?.asJsonArray?.map {
          XmlAttributeDefinitionDeserializer().deserialize(
              it as JsonElement,
              null,
              null) as XmlAttributeDefinition
        }
            .orEmpty()
    )

  }

}
