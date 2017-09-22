package com.aemtools.reference.htl.provider

import com.aemtools.analysis.htl.callchain.typedescriptor.template.TemplateTypeDescriptor
import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isOption
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
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
    val variableName = element as? VariableNameMixin
        ?: return arrayOf()

    if (!variableName.isOption()) {
      return arrayOf()
    }

    val hel = element.findParentByType(HtlElExpressionMixin::class.java)
        ?: return arrayOf()

    val outputType = hel
        .getMainPropertyAccess()
        ?.callChain()
        ?.getLastOutputType()
        as? TemplateTypeDescriptor
        ?: return arrayOf()

    val myName = variableName.variableName()
    val parameterDeclaration = outputType.template.parameterDeclarationElement(element.project, myName)
        ?: return emptyArray()

    return arrayOf(HtlTemplateArgumentReference(parameterDeclaration, element, TextRange.create(0, element.textLength)))
  }

}
