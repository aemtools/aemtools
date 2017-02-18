package com.aemtools.index

import com.aemtools.completion.util.extractTemplateDefinition
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.getHtmlFile
import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.index.dataexternalizer.TemplateDefinitionExternalizer
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex
import java.io.Serializable

/**
 * @author Dmytro_Troynikov
 */
class HtlTemplateIndex : XmlIndex<TemplateDefinition>() {

    companion object {
        val HTL_TEMPLATE_ID: ID<String, TemplateDefinition>
                = ID.create<String, TemplateDefinition>("HtlTemplateIndex")
    }

    override fun getIndexer(): DataIndexer<String, TemplateDefinition, FileContent> {
        return DataIndexer { inputData ->
            val content = inputData.contentAsText

            if (content.contains(DATA_SLY_TEMPLATE)) {

                val file = inputData.psiFile.getHtmlFile()
                        ?: return@DataIndexer mutableMapOf()
                val attributes = file.findChildrenByType(XmlAttribute::class.java)

                val templates = attributes.filter { it.name.startsWith(DATA_SLY_TEMPLATE) }

                val templateDefinitions = templates.flatMap {
                    with(it.extractTemplateDefinition()) {
                        if (this != null) {
                            listOf(this)
                        } else {
                            listOf()
                        }
                    }
                }

                val path = inputData.file.path
                templateDefinitions.forEach { it.fullName = path }

                return@DataIndexer mutableMapOf(*templateDefinitions.map {
                    "$path.$${it.name}" to it
                }.toTypedArray())
            }
            mutableMapOf()
        }
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return FileBasedIndex.InputFilter { it.extension?.endsWith("html") ?: false }
    }

    override fun getValueExternalizer(): DataExternalizer<TemplateDefinition> {
        return TemplateDefinitionExternalizer
    }

    override fun getName(): ID<String, TemplateDefinition> {
        return HTL_TEMPLATE_ID
    }

}

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
}
