package com.aemtools.completion.htl.model

import com.aemtools.completion.util.resolveUseClass
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.xml.XmlAttribute

/**
 * Represents the name of variable and the [XmlAttribute] in which it was declared.
 *
 * @author Dmytro_Troynikov
 */
data class HtlVariableDeclaration(
        /**
         * Declaration [XmlAttribute].
         */
        val xmlAttribute: XmlAttribute,
        /**
         * The name of variable.
         */
        val variableName: String,
        /**
         * The type of Htl declaration attribute.
         */
        val attributeType: DeclarationAttributeType,
        /**
         * The [DeclarationType] of current variable.
         */
        val type: DeclarationType = DeclarationType.VARIABLE,
        /**
         * Certain declarations may have own [ResolutionResult]. By default the result is empty.
         */
        @Deprecated("The resolution result will be removed")
        val resolutionResult: ResolutionResult = ResolutionResult()) {

    /**
     * Convert current [HtlVariableDeclaration] into [LookupElement].
     * @return lookup element
     */
    fun toLookupElement(): LookupElement {
        var result = LookupElementBuilder.create(variableName)

        when (attributeType) {
            DeclarationAttributeType.DATA_SLY_USE -> {
                val varClass = xmlAttribute.resolveUseClass()
                result = result.withTypeText("Sly Use Variable")
                        .withIcon(AllIcons.Nodes.Variable)
                if (!varClass.isNullOrEmpty()) {
                    result = result.withTailText("($varClass)", true)
                }
            }
            DeclarationAttributeType.DATA_SLY_TEST -> {
                val varClass = xmlAttribute.resolveUseClass()
                result = result.withTypeText("Sly Test Variable")
                        .withIcon(AllIcons.Nodes.Variable)
                if (!varClass.isNullOrEmpty()) {
                    result = result.withTailText("($varClass)")
                }
            }
            DeclarationAttributeType.DATA_SLY_LIST -> {
                result = result.withTypeText("Data Sly List")
                        .withIcon(AllIcons.Nodes.Variable)
            }
            DeclarationAttributeType.DATA_SLY_REPEAT -> {
                result = result.withTypeText("Data Sly Repeat")
                        .withIcon(AllIcons.Nodes.Variable)
            }
            DeclarationAttributeType.DATA_SLY_TEMPLATE -> {
                result = result.withTypeText("Template Parameter")
                        .withIcon(AllIcons.Nodes.Parameter)
            }
        }
        return result
    }

}