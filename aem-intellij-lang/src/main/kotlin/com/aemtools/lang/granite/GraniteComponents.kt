package com.aemtools.lang.granite

/**
 * @author Dmytro Troynikov
 */
data class GraniteComponent(
    val fullPath: String,
    val resourceType: String,
    val description: String,
    val fields: List<GraniteField>,
    val attributes: List<GraniteAttribute> = emptyList()
)

/**
 * Represents single granite attribute.
 */
data class GraniteAttribute(
    val name: String,
    val description: String = ""
)

/**
 * Represents granite field.
 */
data class GraniteField(
    val name: String,
    val type: String,
    val default: String,
    val enumeration: List<String> = emptyList()
)

/**
 * List of granite attibutes.
 */
object GraniteAttributes {
  val commonAttrs = GraniteAttribute(
      "granite:commonAtrs"
  )

  val renderCondition = GraniteAttribute(
      "granite:renderCondition"
  )

  val container = GraniteAttribute(
      "granite:container"
  )
}

/**
 * List of standard granite components.
 */
object GraniteComponents {

  val Accordion = GraniteComponent(
      "/libs/granite/ui/components/coral/foundation/accordion",
      "granite/ui/components/coral/foundation/accordion",
      "The accordion.",
      fields = listOf(
          GraniteField(
              "multiple",
              "boolean",
              "false"

          ),
          GraniteField(
              "variant",
              "string",
              "default",
              listOf(
                  "default",
                  "quiet",
                  "large"
              )
          ),
          GraniteField(
              "margin",
              "boolean",
              "false"
          )
      )
  )

}
