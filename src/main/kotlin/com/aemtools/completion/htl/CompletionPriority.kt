package com.aemtools.completion.htl

/**
 * Holder for common completion priority values.
 *
 * @author Dmytro Troynikov
 */
object CompletionPriority {

    val DIALOG_PROPERTY: Double = 1.2
    val PREDEFINED_PARAMETER: Double = 1.1

    val RESOURCE_TYPE: Double = 1.0

    val CLOSE_CLASS: Double = 1.2
    val FAR_CLASS: Double = 0.7

    val CLOSE_TEMPLATE: Double = 1.0
    val FAR_TEMPLATE: Double = 0.5

}