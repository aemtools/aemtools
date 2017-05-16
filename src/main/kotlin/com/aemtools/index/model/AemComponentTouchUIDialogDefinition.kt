package com.aemtools.index.model

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import java.io.Serializable

/**
 * @autor Dmytro Troynikov
 */
data class AemComponentTouchUIDialogDefinition(
        /**
         * Full path to current `.content.xml` file.
         */
        val fullPath: String,

        /**
         * The resourceType of component holder of dialog.
         */
        val resourceType: String,

        val myParameters: List<TouchUIDialogParameterDeclaration>
) : Serializable {

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 1L
    }

    data class TouchUIDialogParameterDeclaration(
            /**
             * `sling:resourceType` value of current parameter.
             */
            val slingResourceType: String,

            /**
             * Name of current parameter.
             */
            val name: String

    ) : Serializable {

        companion object {
            @JvmStatic
            val serialVersionUID: Long = 1L
        }

        fun toLookupElement() : LookupElement =
                LookupElementBuilder.create(name.normalize())
                        .withIcon(AllIcons.Nodes.Parameter)
                        .withTypeText(slingResourceType)

        private fun String.normalize() = if (this.startsWith("./")) {
            this.substringAfter("./")
        } else {
            this
        }

    }
}