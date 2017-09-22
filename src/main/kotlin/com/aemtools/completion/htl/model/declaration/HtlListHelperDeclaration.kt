package com.aemtools.completion.htl.model.declaration

import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlListHelperDeclaration(
    attribute: XmlAttribute,
    variableName: String,
    declarationAttributeType: DeclarationAttributeType)
  : HtlVariableDeclaration(
    attribute,
    variableName,
    declarationAttributeType) {
  companion object {

    /**
     * Builder method for [HtlListHelperDeclaration].
     * Will create list helper for `data-sly-repeat`.
     *
     * @param attribute the declaration attribute
     * @param variableName the variable name
     *
     * @return instance of list helper object
     */
    fun createForRepeat(attribute: XmlAttribute,
                        variableName: String) =
        HtlListHelperDeclaration(attribute, variableName, DeclarationAttributeType.REPEAT_HELPER)

    /**
     * Builder method for [HtlListHelperDeclaration].
     * Will create list helper for `data-sly-list`.
     *
     * @param attribute the declaration attribute
     * @param variableName the variable name
     *
     * @return instance of list helper object
     */
    fun createForList(attribute: XmlAttribute,
                      variableName: String) =
        HtlListHelperDeclaration(attribute, variableName, DeclarationAttributeType.LIST_HELPER)

  }
}
