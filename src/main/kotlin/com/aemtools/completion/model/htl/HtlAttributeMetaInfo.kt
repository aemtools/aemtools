package com.aemtools.completion.model.htl

/**
 * @author Dmytro_Troynikov
 */
data class HtlAttributeMetaInfo(
        val name: String,
        val general: String,
        val element: String?,
        val elementContent: String?,
        val link: String,
        val attributeValue: HtlAttributeValueDescription?,
        val attributeIdentifier: HtlAttributeIdentifierDescription?
)

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

data class HtlAttributeIdentifierDescription(
        val required: String?,
        val description: String?
) {
    fun isNotEmpty() =
            required != null || description != null
}