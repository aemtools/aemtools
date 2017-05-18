package com.aemtools.completion.htl.predefined

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.elements.CallChainSegment
import com.aemtools.analysis.htl.callchain.typedescriptor.PredefinedDescriptionTypeDescriptor
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.util.resourceType
import com.aemtools.constant.const.java.VALUE_MAP
import com.aemtools.index.search.AemComponentSearch
import com.aemtools.util.withPriority
import com.google.gson.annotations.SerializedName
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import javax.swing.Icon

/**
 * @author Dmytro Troynikov
 */
object HtlELPredefined {

    /**
     * List of values applicable as `context` option values.
     *
     * `${@ context='<caret>'}`
     */
    val CONTEXT_VALUES = listOf(
            pc("text", """
                Default for content inside elements.
                Encodes all HTML special characters.
            """),
            pc("html", """
                Filters HTML to meet the AntiSamy policy rules, removing what doesn't match the rules.
            """),
            pc("attribute", """
                Default for attribute values
                Encodes all HTML special characters.
            """),
            pc("uri", """
                To display links and paths Default for _href_ nad _src_ attribute values
                Validates URI for writing as an _href_ or _src_ attribute value, outputs nothing if validation fails.
            """),
            pc("number", """
                To display numbers
                Validates URI for containing an integer, outputs zero if validation fails.
            """),
            pc("attributeName", """
                Default for _data-sly-attribute_ when setting attribute names
                Validates the attribute name, outputs nothing if validation fails.
            """),
            pc("elementName", """
                Default for _data-sly-element_
                Validates the element name, outputs nothing if validation fails.
            """),
            pc("scriptToken", """
                For JS identifiers, literal numbers, or literal strings
                Validates the JavaScript token, outputs nothing if validation fails.
            """),
            pc("scriptString", """
                Within JS strings
                Encodes characters that would break out of the string.
            """),
            pc("scriptComment", """
                Withing JS comments
                Validates the JavaScript comment, outputs nothing if validation fails.
            """),
            pc("styleToken", """
                For CSS identifiers, numbers, dimensions, strings, hex colours or functions
                Validates the CSS token, outputs nothing if validation fails.
            """),
            pc("styleString", """
                Within CSS strings
                Encodes characters that would break out of the string.
            """),
            pc("styleComment", """
                Within CSS comments
                Validates the CSS comment, outputs nothing if validation fails.
            """),
            pc("unsafe", """
                Disables escaping and XSS protection completely.
            """)
    )

    val LIST_AND_REPEAT_HELPER_OBJECT = listOf(
            pc("index", "int", "zero-based counter (0..length-1)"),
            pc("count", "int", "one-based counter (1..length)"),
            pc("count", "boolean", "<b>true</b> for the first element being iterated"),
            pc("middle", "boolean", "<b>true</b> if element being iterated is neither the first nor the last"),
            pc("first", "boolean", "<b>true</b> for the first element being iterated"),
            pc("last", "boolean", "<b>true</b> for the last element being iterated"),
            pc("odd", "boolean", "<b>true</b> if index is odd"),
            pc("even", "boolean", "<b>true</b> if index is even")
    )

    val DEFAULT_PROPERTIES = listOf(
            pc("jcr:title",
                    "java.lang.String",
                    "String value of <b>jcr:title</b> property, or empty String if such property does not exist."),
            pc("jcr:description", "java.lang.String",
                    "String value of <b>jcr:description</b> property, or empty String if such property does not exist."),
            pc("jcr:primaryType",
                    "java.lang.String",
                    "String defines primary type of the component."),
            pc("jcr:mixinTypes",
                    "java.lang.String[]",
                    "String array of mixin types"),
            pc("jcr:createdBy",
                    "java.lang.String",
                    "ID of user, created the component or page."),
            pc("cq:lastReplicationAction",
                    "java.lang.String",
                    "Last replication action performed on this node."),
            pc("cq:lastReplicatedBy",
                    "java.lang.String",
                    "ID of user, which replicated current node the last time."),
            pc("jcr:lastModifiedBy",
                    "java.lang.String",
                    "ID of user, which modified current node the last time."),
            pc("jcr:lastModified",
                    "java.util.Calendar",
                    "Date and time of last modified action."),
            pc("cq:lastReplicated",
                    "java.util.Calendar",
                    "Date and time of last replication action."),
            pc("sling:resourceType",
                    "java.lang.String",
                    "String defines the resource type of the component or page.")
    )

    fun addPredefined(callChain: CallChain,
                      element: PsiElement,
                      currentSegment: CallChainSegment,
                      currentElement: CallChainElement,
                      resolutionResult: ResolutionResult): ResolutionResult {
        val psiClass = resolutionResult.psiClass
        if (psiClass != null) {
            if (psiClass.qualifiedName == VALUE_MAP) {
                val currentChain = currentSegment.chainElements()
                val previousElement = currentChain.getOrNull(
                        currentChain.indexOf(currentElement) - 1
                ) ?: return resolutionResult

                if (previousElement.name == "pageProperties"
                        || previousElement.name == "inheritedPageProperties") {
                    return resolutionResult.add(DEFAULT_PROPERTIES.map {
                        it.toLookupElement()
                                .withPriority(0.9)
                    })
                }

                if (previousElement.name == "properties") {
                    val result = if (resolutionResult.predefined != null) {
                        resolutionResult
                    } else {
                        resolutionResult.add(DEFAULT_PROPERTIES.map {
                            it.toLookupElement()
                                    .withPriority(0.9)
                        })
                    }
                    val myResourceType = element.containingFile.originalFile.virtualFile.resourceType()
                            ?: return result

                    val touchUiDialog = AemComponentSearch.findTouchUIDialogByResourceType(myResourceType, element.project)
                    if (touchUiDialog != null) {
                        return result.add(
                                touchUiDialog.myParameters.map {
                                    it.toLookupElement()
                                            .withPriority(1.toDouble())
                                }
                        )
                    }

                    val classicDialog = AemComponentSearch.findClassicDialogByResourceType(myResourceType, element.project)
                    if (classicDialog != null) {
                        return result.add(
                                classicDialog.myParameters.map {
                                    it.toLookupElement()
                                            .withPriority(1.toDouble())
                                }
                        )
                    }
                    return result
                }
            }
        }

        return resolutionResult
    }

}

private fun pc(completionText: String,
               type: String? = null,
               documentation: String? = null) =
        PredefinedCompletion(completionText, type, documentation)

data class PredefinedCompletion(
        @SerializedName(value = "name")
        val completionText: String,
        val type: String? = null,
        @SerializedName(value = "description")
        val documentation: String? = null,
        val typeText: String? = null,
        val icon: Icon? = AllIcons.Nodes.Parameter
) {
    fun toLookupElement(): LookupElement {
        var result = LookupElementBuilder.create(completionText)
                .withIcon(icon ?: AllIcons.Nodes.Parameter)
        if (type != null) {
            result = result.withTypeText(type)
        }
        return result
    }

    fun asTypeDescriptor(): PredefinedDescriptionTypeDescriptor =
            PredefinedDescriptionTypeDescriptor(this)
}

/**
 * Add lookup elements from given list to current resolution result.
 * @param completionVariants variants to add
 * @return new [ResolutionResult] object
 */
fun ResolutionResult.add(completionVariants: List<LookupElement>): ResolutionResult {
    val myVariants = this.predefined
    if (myVariants == null) {
        return ResolutionResult(this.psiClass, completionVariants)
    } else {
        return ResolutionResult(this.psiClass, myVariants + completionVariants)
    }
}