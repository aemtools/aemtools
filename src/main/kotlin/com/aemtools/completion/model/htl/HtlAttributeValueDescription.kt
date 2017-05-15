package com.aemtools.completion.model.htl

data class HtlAttributeValueDescription(
        val required: String?,
        val type: List<String>?,
        val description: String?
) {
    fun isNotEmpty() =
            required != null || description != null || (type != null && type.isNotEmpty())

    fun printType() : String? {
        return if (type == null || type.isEmpty()) {
            null
        } else {
            if (type.size == 1) {
                type.first()
            } else {
                type.joinToString(", ", " [", "] ")
            }
        }
    }
}