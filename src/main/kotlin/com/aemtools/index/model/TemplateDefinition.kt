package com.aemtools.index.model

import com.aemtools.completion.util.normalizeToJcrRoot
import java.io.Serializable

/**
 * @author Dmytro Troynikov
 */

/**
 *  Container of template definition data. (`data-sly-template.name="${@ param1, param2}"
 */
data class TemplateDefinition(
        /**
         * Full name
         */
        var fullName: String?,
        /**
         * The name of the template
         */
        val name: String,
        /**
         * List of parameters declared in template.
         * e.g.
         *
         * ```
         * <div data-sly-template.template="${@ param1, param2}> -> [param1, param2]
         * ```
         *
         */
        val parameters: List<String>) : Serializable {

    val containingDirectory: String
        get() {
            val _fullName = fullName
                    ?: return ""
            return _fullName.substring(0, _fullName.lastIndexOf("/"))
        }

    /**
     * Return path starting from "/apps"
     */
    val normalizedPath: String
        get() {
            val _path = fullName
            return if (_path != null) {
                return _path.normalizeToJcrRoot()
            } else {
                ""
            }
        }

    /**
     * The name of html file
     */
    val fileName: String
        get() {
            val _path = fullName
            return _path?.substring(_path.lastIndexOf("/") + 1) ?: ""
        }

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 1L
    }

}
