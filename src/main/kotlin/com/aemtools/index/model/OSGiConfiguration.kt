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

    private fun removeSuffixIfPresent(fqn: String): String = if (fqn.indexOf("-") != -1) {
        fqn.substring(0, fqn.lastIndexOf("-"))
    } else {
        fqn
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
            val result = path.split("/").find { it.startsWith("config") }
                    ?.split(".")
                    ?.filterNot { it == "config" }

            return if (result == null || result.isEmpty()) {
                listOf("default")
            } else {
                result
            }
        }

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 1L
    }
}

/**
 * Sort current [OSGiConfiguration] collection by mods.
 *
 * @return collection sorted by mods
 */
fun List<OSGiConfiguration>.sortByMods(): List<OSGiConfiguration> =
        this.sortedWith(kotlin.Comparator<OSGiConfiguration> { o1, o2 ->
            when {
                o1.mods.joinToString(separator = "") { it } == "default" -> -1
                o2.mods.joinToString(separator = "") { it } == "default" -> 1
                else -> o1.mods.joinToString(separator = "") { it }
                        .compareTo(o2.mods.joinToString(separator = "") { it })
            }
        })
