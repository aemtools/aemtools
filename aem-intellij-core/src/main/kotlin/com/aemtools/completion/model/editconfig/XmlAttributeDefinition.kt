package com.aemtools.completion.model.editconfig

import com.aemtools.completion.model.JsTypeInfo

/**
 * @author Dmytro Primshyts
 */
data class XmlAttributeDefinition(
    /**
     * The name of attribute
     */
    val name: String,
    val type: JsTypeInfo,
    val values: List<String>,
    val delimiter: String
)
