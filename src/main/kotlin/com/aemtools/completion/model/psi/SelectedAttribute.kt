package com.aemtools.completion.model.psi

/**
 * Holds data of currently selected attribute and denotes which particular part of attribute is selected
 * @author Dmytro_Troynikov
 */
data class SelectedAttribute(
    val name: String,
    val value: String?,
    val nameSelected: Boolean,
    val valueSelected: Boolean
)
