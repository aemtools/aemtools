package com.aemtools.codeinsight.htl.model

import com.aemtools.index.model.TemplateDefinition
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Primshyts
 */
class HtlTemplateDeclaration(
    val templateDefinition: TemplateDefinition,
    val options: List<HtlTemplateParameterDeclaration>,
    xmlAttribute: XmlAttribute,
    variableName: String
) : HtlVariableDeclaration(
    xmlAttribute,
    variableName,
    DeclarationAttributeType.DATA_SLY_TEMPLATE
)
