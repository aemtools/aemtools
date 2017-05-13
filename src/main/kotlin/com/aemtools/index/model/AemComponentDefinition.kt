package com.aemtools.index.model

import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.constant.const.JCR_DESCRIPTION
import com.aemtools.constant.const.JCR_TITLE
import com.aemtools.constant.const.SLING_RESOURCE_SUPER_TYPE
import com.aemtools.constant.const.aem_component_declaration.COMPONENT_GROUP
import com.aemtools.constant.const.aem_component_declaration.CQ_ICON
import com.aemtools.constant.const.aem_component_declaration.IS_CONTAINER
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.xml.XmlTag
import org.apache.commons.lang.BooleanUtils
import java.io.Serializable

/**
 * Represents content of AEM component descriptor (`.content.xml
 * @author Dmytro Troynikov
 */
data class AemComponentDefinition(

        /**
         * Represents the `jcr:title` attribute.
         */
        val title: String?,

        /**
         * Represents the `jcr:description` attribute.
         */
        val description: String?,

        /**
         * Full path of current component on file system.
         */
        val fullPath: String,

        /**
         * Represents the `sling:resourceSuperType` attribute.
         */
        val resourceSuperType: String?,

        /**
         * Represents the `componentGroup` attribute.
         */
        val componentGroup: String?,

        /**
         * Represents the `cq:isContainer` attribute.
         *
         * Default: *false*
         */
        val isContainer: Boolean = false,

        /**
         * Represents the `cq:icon` attribute.
         */
        val cqIcon: String? = null

) : Serializable {

    /**
     * Normalize path to *jcr_root*
     *
     * @see [normalizeToJcrRoot]
     * @return path normalized to jcr_root
     */
    fun pathNormalizedToJcrRoot(): String
            = fullPath.normalizeToJcrRoot().substringBeforeLast("/")


    fun componentName(): String
            = fullPath
            .substringBeforeLast("/")
            .substringAfterLast("/")

    companion object {

        @JvmStatic
        val serialVersionUID: Long = 1L

        /**
         * Extract [AemComponentDefinition] from given [XmlTag].
         *
         * Note: if component doesn't have `jcr:title` or `componentGroup` *null* will be returned.
         *
         * @param tag the tag
         * @param fullPath path to component
         * @return aem component definition
         */
        fun fromTag(tag: XmlTag, fullPath: String): AemComponentDefinition? {
            val title = tag.getAttributeValue(JCR_TITLE)
                    ?: return null
            val group = tag.getAttributeValue(COMPONENT_GROUP)
                    ?: return null
            return AemComponentDefinition(
                    title = title,
                    description = tag.getAttributeValue(JCR_DESCRIPTION),
                    fullPath = fullPath,
                    resourceSuperType = tag.getAttributeValue(SLING_RESOURCE_SUPER_TYPE),
                    componentGroup = group,
                    isContainer = BooleanUtils.toBoolean(
                            tag.getAttributeValue(IS_CONTAINER)
                    ),
                    cqIcon = tag.getAttributeValue(CQ_ICON)
            )
        }

        /**
         * Convert current [AemComponentDefinition] to [LookupElement].
         *
         * @receiver [AemComponentDefinition]
         * @return lookup element
         */
        fun AemComponentDefinition.toLookupElement(): LookupElement =
                LookupElementBuilder.create(pathNormalizedToJcrRoot())
                        .withTypeText("AEM Component")
                        .withPresentableText(componentName())
                        .withTailText("($title)", true)
                        // todo find appropriate icon
                        .withIcon(AllIcons.Nodes.Jsf.Component)

    }

}