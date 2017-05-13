package com.aemtools.index.model

import com.aemtools.constant.const.JCR_DESCRIPTION
import com.aemtools.constant.const.JCR_TITLE
import com.aemtools.constant.const.SLING_RESOURCE_SUPER_TYPE
import com.aemtools.constant.const.aem_component_declaration.COMPONENT_GROUP
import com.aemtools.constant.const.aem_component_declaration.CQ_ICON
import com.aemtools.constant.const.aem_component_declaration.IS_CONTAINER
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

    companion object {
        fun fromTag(tag: XmlTag, fullPath: String): AemComponentDefinition =
                AemComponentDefinition(
                        title = tag.getAttributeValue(JCR_TITLE),
                        description = tag.getAttributeValue(JCR_DESCRIPTION),
                        fullPath = fullPath,
                        resourceSuperType = tag.getAttributeValue(SLING_RESOURCE_SUPER_TYPE),
                        componentGroup = tag.getAttributeValue(COMPONENT_GROUP),
                        isContainer = BooleanUtils.toBoolean(
                                tag.getAttributeValue(IS_CONTAINER)
                        ),
                        cqIcon = tag.getAttributeValue(CQ_ICON)
                )


    }

}