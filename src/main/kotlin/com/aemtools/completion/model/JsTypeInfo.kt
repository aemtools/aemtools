package com.aemtools.completion.model

/**
 * The type of attribute (e.g. __Boolean__)
 * Can be mixed, the | sign is used as separator
 *
 * @author Dmytro_Troynikov
 */
class JsTypeInfo(
    val types: List<String>
) {
  companion object {

    /**
     * Create empty [JsTypeInfo].
     *
     * @return instance of empty js type info
     */
    fun empty() = JsTypeInfo(listOf())
  }
}
