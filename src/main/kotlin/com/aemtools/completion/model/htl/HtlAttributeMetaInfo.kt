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