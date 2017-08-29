package com.aemtools.index.model.dialog.parameter

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons

/**
 * Touch UI parameter declaration model.
 *
 * @property slingResourceType Sling resource type of underlying parameter (`sling:resourceType`).
 *
 * @author Dmytro Troynikov
 */
data class TouchUIDialogParameterDeclaration(
        val slingResourceType: String,
        override val name: String
) : BaseParameterDeclaration(
        name
) {

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 1L
    }

    /**
     * Convert current parameter declaration into [LookupElement].
     *
     * @return new lookup element
     */
    fun toLookupElement(): LookupElement =
            LookupElementBuilder.create(name.normalize())
                    .withIcon(AllIcons.Nodes.Parameter)
                    .withTailText("($slingResourceType)", true)
                    .withTypeText("Dialog")

}