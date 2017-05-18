package com.aemtools.index.model

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import java.io.Serializable

/**
 * @author Dmytro Troynikov
 */
data class AemComponentClassicDialogDefinition(
        /**
         * Full path to current `dialog.xml` file.
         */
        val fullPath: String,

        /**
         * The resourceType of component holder of dialog.
         */
        val resourceType: String,

        /**
         * List of parameters declared in current dialog.
         */
        val myParameters: List<ClassicDialogParameterDeclaration>
)
    : Serializable {

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 1L
    }

    data class ClassicDialogParameterDeclaration(
            /**
             * Xtype of ext component.
             */
            val xtype: String,
            /**
             * The name of the component.
             */
            val name: String
    ) : Serializable {

        companion object {
            @JvmStatic
            val serialVersionUID: Long = 1L
        }

        fun toLookupElement(): LookupElement =
                LookupElementBuilder.create(name.normalize())
                        .withIcon(AllIcons.Nodes.Parameter)
                        .withTypeText(xtype)

        private fun String.normalize() = if (this.startsWith("./")) {
            this.substringAfter("./")
        } else {
            this
        }

    }

}