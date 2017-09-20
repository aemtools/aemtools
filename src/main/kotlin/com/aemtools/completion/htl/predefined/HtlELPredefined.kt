package com.aemtools.completion.htl.predefined

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
   * List of completion variants for list helper variables.
   */
  val LIST_AND_REPEAT_HELPER_OBJECT = listOf(
      pc("index", "int", "zero-based counter (0..length-1)"),
      pc("count", "int", "one-based counter (1..length)"),
      pc("first", "boolean", "<b>true</b> for the first element being iterated"),
      pc("middle", "boolean", "<b>true</b> if element being iterated is neither the first nor the last"),
      pc("last", "boolean", "<b>true</b> for the last element being iterated"),
      pc("odd", "boolean", "<b>true</b> if index is odd"),
      pc("even", "boolean", "<b>true</b> if index is even")
  )

}

private fun pc(completionText: String,
               type: String? = null,
               documentation: String? = null) =
    PredefinedCompletion(completionText, type, documentation)
