package com.aemtools.completion.model.htl

/**
 * @author Dmytro Troynikov
 */
data class HtlAttributeIdentifierDescription(
    val required: String?,
    val description: String?
) {

  /**
   * Check if current attribute identifier description is not empty.
   *
   * @return *true* if description is not empty, *false* otherwise
   */
  fun isNotEmpty() =
      required != null || description != null
}
