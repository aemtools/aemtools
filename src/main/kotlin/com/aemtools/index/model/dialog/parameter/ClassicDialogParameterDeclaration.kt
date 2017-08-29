package com.aemtools.index.model.dialog.parameter

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons

/**
 * Classic dialog parameter declaration model.
 *
 * @property xtype The xtype type of underlying parameter.
 *
 * @author Dmytro Troynikov
 */
data class ClassicDialogParameterDeclaration(
        val xtype: String,
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
                    .withTailText("($xtype)", true)
                    .withTypeText("Dialog")

}