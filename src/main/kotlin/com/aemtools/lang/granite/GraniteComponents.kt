package com.aemtools.lang.granite

/**
 * @author Dmytro Troynikov
 */
data class GraniteComponent(
    val fullPath: String,
    val resourceType: String,
    val description: String,
    val fields: List<GraniteField>,
    val attributes: List<GraniteAttribute>
)

data class GraniteAttribute(
    val name: String,
    val description: String = ""
)

data class GraniteField(
    val name: String,
    val type: String,
    val default: String,
    val enumeration: List<String> = emptyList()
)

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
