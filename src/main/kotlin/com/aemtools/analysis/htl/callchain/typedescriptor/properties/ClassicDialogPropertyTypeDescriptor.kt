package com.aemtools.analysis.htl.callchain.typedescriptor.properties

import com.aemtools.analysis.htl.callchain.typedescriptor.base.BaseTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.index.model.dialog.AemComponentClassicDialogDefinition
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
class ClassicDialogPropertyTypeDescriptor(
    val name: String,
    val element: PsiElement,
    val classicDialogDefinition: AemComponentClassicDialogDefinition)
  : BaseTypeDescriptor() {

  override fun referencedElement(): PsiElement? =
      classicDialogDefinition.declarationElement(name, element.project)

  override fun myVariants(): List<LookupElement> = emptyList()

  override fun subtype(identifier: String): TypeDescriptor
      = TypeDescriptor.empty()

}
