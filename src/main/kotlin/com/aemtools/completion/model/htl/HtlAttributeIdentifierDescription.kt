package com.aemtools.completion.model.htl

data class HtlAttributeIdentifierDescription(
        val required: String?,
        val description: String?
) {
    fun isNotEmpty() =
            required != null || description != null
}