package com.aemtools.completion.model.editconfig

/**
 * @author Dmytro_Troynikov
 */
data class XmlTagDefinition(

    /**
     * Name of xml tag.
     */
    val name: String,

    /**
     * List of possible child nodes.
     */
    val childNodes: List<String>,

    /**
     * List of possible attributes.
     */
    val attributes: List<XmlAttributeDefinition>
) {
  companion object {
    /**
     * Create empty [XmlTagDefinition] object.
     *
     * @return instance of empty xml tag definition object
     */
    fun empty() = XmlTagDefinition("", listOf(), listOf())
  }
}
