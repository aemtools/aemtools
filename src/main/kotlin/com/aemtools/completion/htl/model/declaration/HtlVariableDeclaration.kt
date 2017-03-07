package com.aemtools.completion.htl.model.declaration

import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.completion.htl.model.declaration.DeclarationAttributeType
import com.aemtools.completion.htl.model.declaration.DeclarationType
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.htl.model.declaration.UseType
import com.aemtools.completion.htl.predefined.HtlELPredefined
import com.aemtools.completion.util.*
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.index.TemplateDefinition
import com.aemtools.index.search.HtlTemplateSearch
import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiClass
import com.intellij.psi.xml.XmlAttribute

/**
 * Represents the name of variable and the [XmlAttribute] in which it was declared.
 *
 * @author Dmytro_Troynikov
 */
open class HtlVariableDeclaration internal constructor(
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

    companion object {
        fun create(attribute: XmlAttribute): List<HtlVariableDeclaration> {
            val htlAttributeName = attribute.htlAttributeName()
            val htlVariableName = attribute.htlVariableName()
            return when {
                htlAttributeName == DATA_SLY_USE
                        && htlVariableName != null -> {
                    listOf(
                            HtlUseVariableDeclaration(
                                    attribute,
                                    htlVariableName,
                                    DeclarationAttributeType.DATA_SLY_USE
                            )
                    )
                }
                htlAttributeName == DATA_SLY_TEST
                        && htlVariableName != null -> {
                    listOf(
                            HtlVariableDeclaration(
                                    attribute,
                                    htlVariableName,
                                    DeclarationAttributeType.DATA_SLY_TEST,
                                    DeclarationType.VARIABLE
                            )
                    )
                }
                htlAttributeName == DATA_SLY_LIST -> {
                    val (item, itemList) = extractItemAndItemListNames(attribute.name)

                    listOf(
                            HtlVariableDeclaration(
                                    attribute,
                                    item,
                                    DeclarationAttributeType.DATA_SLY_LIST,
                                    DeclarationType.ITERABLE
                            ),
                            HtlVariableDeclaration(
                                    attribute,
                                    itemList,
                                    DeclarationAttributeType.DATA_SLY_LIST,
                                    DeclarationType.VARIABLE,
                                    ResolutionResult(
                                            predefined = HtlELPredefined.DATA_SLY_LIST_REPEAT_LIST_FIELDS
                                    )
                            )
                    )
                }
                htlAttributeName == DATA_SLY_REPEAT -> {
                    val (item, itemList) = extractItemAndItemListNames(attribute.name)

                    listOf(
                            HtlVariableDeclaration(
                                    attribute,
                                    item,
                                    DeclarationAttributeType.DATA_SLY_REPEAT,
                                    DeclarationType.ITERABLE
                            ),
                            HtlVariableDeclaration(
                                    attribute,
                                    itemList,
                                    DeclarationAttributeType.DATA_SLY_REPEAT,
                                    DeclarationType.ITERABLE,
                                    ResolutionResult(
                                            predefined = HtlELPredefined.DATA_SLY_LIST_REPEAT_LIST_FIELDS
                                    )
                            )
                    )
                }

                htlAttributeName == DATA_SLY_TEMPLATE -> {
                    val templateParameters = attribute.extractTemplateParameters()
                    templateParameters.map { parameter ->
                        HtlVariableDeclaration(
                                attribute,
                                parameter,
                                DeclarationAttributeType.DATA_SLY_TEMPLATE,
                                DeclarationType.VARIABLE
                        )
                    }
                }

                else -> listOf()
            }
        }

    }

}