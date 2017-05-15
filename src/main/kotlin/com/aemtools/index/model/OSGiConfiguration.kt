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
     * com.test.MyService.xml -> ""
     *
     * @return the suffix or empty string
     */
    fun suffix(): String = if (fileName.contains("-")) {
        fileName.substring(fileName.indexOf("-") + 1, fileName.lastIndexOf("."))
    } else {
        ""
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
                o1.mods.size != o2.mods.size ->
                    o1.mods.size - o2.mods.size
                o1.modsConcatenated() == o2.modsConcatenated() ->
                    o1.suffix().compareTo(o2.suffix())
                o1.modsConcatenated() == "default" -> -1
                o2.modsConcatenated() == "default" -> 1
                else -> o1.modsConcatenated()
                        .compareTo(o2.modsConcatenated())
            }
        })

fun OSGiConfiguration.modsConcatenated(): String =
        mods.joinToString(separator = "") { it }
