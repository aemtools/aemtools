package com.aemtools.completion.htl.predefined

import com.aemtools.analysis.htl.callchain.elements.CallChain
import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.elements.CallChainSegment
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.constant.const.java.VALUE_MAP
import com.google.gson.annotations.SerializedName
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

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

    /**
     * List of [LookupElement] objects to be suggested as variants for additional
     * "List" variable spawned by `data-sly-list` and `data-sly-repeat` attributes.
     */
    val DATA_SLY_LIST_REPEAT_LIST_FIELDS: List<LookupElement> = listOf(
            LookupElementBuilder.create("index"),
            LookupElementBuilder.create("count"),
            LookupElementBuilder.create("first"),
            LookupElementBuilder.create("middle"),
            LookupElementBuilder.create("last"),
            LookupElementBuilder.create("odd"),
            LookupElementBuilder.create("even")
    )

    val DEFAULT_PROPERTIES = listOf(
            pc("jcr:title"),
            pc("jcr:description"),
            pc("jcr:primaryType"),
            pc("jcr:mixinTypes"),
            pc("jcr:createdBy"),
            pc("cq:lastReplicationAction"),
            pc("cq:lastReplicatedBy"),
            pc("jcr:lastModifiedBy"),
            pc("jcr:lastModified"),
            pc("cq:lastReplicated"),
            pc("sling:resourceType")
    )

    fun addPredefined(callChain: CallChain,
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

                if (previousElement.name == "properties"
                        || previousElement.name == "pageProperties"
                        || previousElement.name == "inheritedPageProperties") {
                    return resolutionResult.add(DEFAULT_PROPERTIES.map { it.toLookupElement() })
                }
            }
        }

        return resolutionResult
    }

}

private fun pc(completionText: String, documentation: String? = null) =
        PredefinedCompletion(completionText, documentation)

data class PredefinedCompletion(
        @SerializedName(value = "name")
        val completionText: String,
        val type: String? = null,
        @SerializedName(value = "description")
        val documentation: String? = null
) {
    fun toLookupElement(): LookupElement {
        val result = LookupElementBuilder.create(completionText)
        if (type != null) {
            result.withTypeText(type, true)
        }
        return result
    }
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