package com.aemtools.index.model

import java.io.Serializable

/**
 * @author Dmytro_Troynikov
 */
data class OSGiConfiguration(val path: String,
                             val parameters: Map<String, String?>) : Serializable {

    val fullQualifiedName: String
            get() {
                return path.substring(path.lastIndexOf("/") + 1,
                        path.lastIndexOf("."))
            }

    val fileName: String
            get() {
                return path.substring(path.lastIndexOf("/") + 1)
            }


    val mods: List<String>
        get() {
            return path.split("/").find { it.startsWith("config") }
                    ?.split(".")
                    .orEmpty()
        }

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 1L
    }

}