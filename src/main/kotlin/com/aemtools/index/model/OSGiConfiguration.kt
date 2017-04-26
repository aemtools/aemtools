package com.aemtools.index.model

import com.intellij.psi.xml.XmlFile
import java.io.Serializable

/**
 * @author Dmytro_Troynikov
 */
data class OSGiConfiguration(val path: String,
                             val parameters: Map<String, String?>,
                             var xmlFile: XmlFile? = null) : Serializable {

    /**
     * Full qualified name of associated OSGi Service or Service factory.
     */
    val fullQualifiedName: String
        get() {
            return removeSuffixIfPresent(
                    fileName.substring(0, fileName.lastIndexOf(".")))
        }

    private fun removeSuffixIfPresent(fqn: String): String {
        return if (fqn.indexOf("-") != -1) {
            fqn.substring(0, fqn.lastIndexOf("-"))
        } else {
            fqn
        }
    }

    /**
     * Return name suffix.
     *
     * com.test.MyService-first.xml -> "first"
     * com.test.MyService.xml -> null
     *
     */
    fun suffix(): String? {
        return null
    }

    /**
     * File name of current OSGi Configuration.
     */
    val fileName: String
        get() {
            return path.substring(path.lastIndexOf("/") + 1)
        }

    /**
     * List of run modes.
     */
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