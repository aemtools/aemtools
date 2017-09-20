package com.aemtools.analysis.htl.callchain.typedescriptor.properties

import com.aemtools.analysis.htl.callchain.typedescriptor.base.BaseTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.index.model.dialog.AemComponentTouchUIDialogDefinition
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
class TouchDialogPropertyTypeDescriptor(
    val name: String,
    val element: PsiElement,
    val touchDialogDefinition: AemComponentTouchUIDialogDefinition)
  : BaseTypeDescriptor() {

  override fun referencedElement(): PsiElement? =
      touchDialogDefinition.declarationElement(name, element.project)

  override fun myVariants(): List<LookupElement> = emptyList()

  override fun subtype(identifier: String): TypeDescriptor
      = TypeDescriptor.empty()

}
