package com.aemtools.index.model

import java.io.Serializable

/**
 * OSGi configuration model used by indexing infrastructure.
 *
 * @author Dmytro Primshyts
 */
data class OSGiConfigurationIndexModel(
    val path: String,
    val parameters: Map<String, String?>
) : Serializable {
  companion object {
    @JvmStatic
    val serialVersionUID: Long = 1L
  }
}
