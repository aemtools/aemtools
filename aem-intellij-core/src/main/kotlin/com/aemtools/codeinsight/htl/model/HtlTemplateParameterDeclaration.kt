package com.aemtools.codeinsight.htl.model

import com.aemtools.lang.htl.psi.HtlVariableName
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlTemplateParameterDeclaration(
    val htlVariableNameElement: HtlVariableName,
    xmlAttribute: XmlAttribute,
    variableName: String
) : HtlVariableDeclaration(
    xmlAttribute,
    variableName,
    DeclarationAttributeType.DATA_SLY_TEMPLATE_PARAMETER
)
