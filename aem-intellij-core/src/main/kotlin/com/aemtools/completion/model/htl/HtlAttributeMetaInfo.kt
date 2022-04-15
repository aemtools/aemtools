package com.aemtools.completion.model.htl

/**
 * @author Dmytro Primshyts
 */
data class HtlAttributeMetaInfo(

    /**
     * Name of attribute.
     */
    val name: String,

    /**
     * General info.
     */
    val general: String,

    /**
     * Visibility of tag holder of
     * htl attribute.
     */
    val element: String?,

    /**
     * Visibility of tag content.
     */
    val elementContent: String?,

    /**
     * Link to htl specification.
     */
    val link: String,

    /**
     * Description of attribute value.
     */
    val attributeValue: HtlAttributeValueDescription?,

    /**
     * Description of attribute identifier
     * (variable spawned by the attribute).
     */
    val attributeIdentifier: HtlAttributeIdentifierDescription?

)
