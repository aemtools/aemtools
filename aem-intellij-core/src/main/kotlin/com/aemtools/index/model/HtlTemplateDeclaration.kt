package com.aemtools.index.model

import com.aemtools.codeinsight.htl.model.DeclarationAttributeType
import com.aemtools.codeinsight.htl.model.HtlTemplateParameterDeclaration
import com.aemtools.codeinsight.htl.model.HtlVariableDeclaration
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
