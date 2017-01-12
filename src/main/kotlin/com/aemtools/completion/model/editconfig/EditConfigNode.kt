package com.aemtools.completion.model.editconfig

/**
 * @author Dmytro_Troynikov
 */
data class EditConfigNode (
        val name: String,
        val childNodes: Array<String>,
        val attributes: Array<XmlAttributeDefinition>
){}



