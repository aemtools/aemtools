package com.aemtools.reference.htl.provider

import com.aemtools.analysis.htl.callchain
import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateTypeDescriptor
import com.aemtools.common.util.findParentByType
import com.aemtools.lang.util.isOption
import com.aemtools.reference.htl.reference.HtlTemplateArgumentReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object DataSlyCallPropertiesReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement,
                                      context: ProcessingContext): Array<PsiReference> {
    val variableName = element as? com.aemtools.lang.htl.psi.mixin.VariableNameMixin
        ?: return arrayOf()

    if (!variableName.isOption()) {
      return arrayOf()
    }

    val hel = element.findParentByType(com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin::class.java)
        ?: return arrayOf()

    val outputType = hel
        .getMainPropertyAccess()
        ?.callchain()
        ?.getLastOutputType()
        as? TemplateTypeDescriptor
        ?: return arrayOf()

    val myName = variableName.variableName()
    val parameterDeclaration = outputType.template.parameterDeclarationElement(element.project, myName)
        ?: return emptyArray()

    return arrayOf(HtlTemplateArgumentReference(parameterDeclaration, element, TextRange.create(0, element.textLength)))
  }

}
