package com.aemtools.completion.htl.predefined

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
}

private fun pc(completionText: String, documentation: String? = null) =
        PredefinedCompletion(completionText, documentation)

data class PredefinedCompletion(
        @SerializedName(value = "name")
        val completionText: String,
        val type: String? = null,
        @SerializedName(value = "description")
        val documentation: String? = null
)