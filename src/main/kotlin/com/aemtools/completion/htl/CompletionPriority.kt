package com.aemtools.completion.htl

/**
 * Holder for common completion priority values.
 *
 * @author Dmytro Troynikov
 */
object CompletionPriority {

  const val DIALOG_PROPERTY: Double = 1.2
  const val PREDEFINED_PARAMETER: Double = 1.1

  const val RESOURCE_TYPE: Double = 1.0

  const val CLOSE_CLASS: Double = 1.2
  const val FAR_CLASS: Double = 0.7

  const val CLOSE_TEMPLATE: Double = 1.0
  const val FAR_TEMPLATE: Double = 0.5

  const val VARIABLE_OUTSIDER: Double = -1.0
  const val VARIABLE_BASE: Double = 1.0
  const val CONTEXT_OBJECT: Double = 0.0

  const val CONTAINING_CLASS: Double = 1.0
  const val OBJECT_CLASS: Double = 0.0
  const val MIDDLE_CLASS: Double = 0.5

}
