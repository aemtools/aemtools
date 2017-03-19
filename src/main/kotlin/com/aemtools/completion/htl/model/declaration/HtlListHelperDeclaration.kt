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
        fun createForRepeat(attribute: XmlAttribute,
                            variableName: String) =
                HtlListHelperDeclaration(attribute, variableName, DeclarationAttributeType.REPEAT_HELPER)

        fun createForList(attribute: XmlAttribute,
                          variableName: String) =
                HtlListHelperDeclaration(attribute, variableName, DeclarationAttributeType.LIST_HELPER)
    }
}