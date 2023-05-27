package com.aemtools.index.indexer.osgi.impl

import com.aemtools.index.indexer.osgi.OSGiPropertyMapper
import com.intellij.json.psi.*
import com.intellij.json.psi.impl.JsonPsiImplUtils

object JsonOSGiPropertyMapper : OSGiPropertyMapper<JsonProperty> {
  override fun map(psiElement: JsonProperty): Pair<String, String?> {
    val value = when (psiElement.value) {
      is JsonStringLiteral,
      is JsonBooleanLiteral,
      is JsonNumberLiteral -> getJsonLiteralStringValue(psiElement.value as JsonLiteral)

      is JsonArray -> (psiElement.value as JsonArray).valueList
          .filterIsInstance<JsonLiteral>()
          .joinToString(separator = ",") { getJsonLiteralStringValue(it) ?: it.text }

      else -> null
    }
    return psiElement.name to value
  }

  private fun getJsonLiteralStringValue(jsonLiteral: JsonLiteral): String? =
      when (jsonLiteral) {
        is JsonStringLiteral -> JsonPsiImplUtils.getValue(jsonLiteral)
        is JsonBooleanLiteral -> JsonPsiImplUtils.getValue(jsonLiteral).toString()
        is JsonNumberLiteral -> JsonPsiImplUtils.getValue(jsonLiteral).toString()
        else -> null
      }
}
