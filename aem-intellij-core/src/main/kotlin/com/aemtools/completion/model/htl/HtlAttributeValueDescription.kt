package com.aemtools.completion.model.htl

/**
 * Htl attribute value description model.
 */
data class HtlAttributeValueDescription(
    val required: String?,
    val type: List<String>?,
    val description: String?
) {
  /**
   * Check if current htl attribute value description is not empty.
   *
   * @return *true* if description is not empty, *false* otherwise
   */
  fun isNotEmpty() =
      required != null || description != null || (type != null && type.isNotEmpty())

  /**
   * Return string representation of current value.
   *
   * @return string representation, *null* if description is empty
   */
  fun printType(): String? {
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
