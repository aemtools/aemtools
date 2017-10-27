package com.aemtools.reference.common.reference

import com.aemtools.analysis.htl.callchain.elements.CallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiUnresolvedTypeDescriptor
import com.aemtools.lang.htl.psi.mixin.AccessIdentifierMixin
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiReferenceBase

/**
 * @author Dmytro Troynikov
 */
class HtlPropertyAccessReference(
    val propertyAccess: PropertyAccessMixin,
    val callChainElement: CallChainElement,
    textRange: TextRange,
    val referencedElement: PsiElement,
    soft: Boolean = true
) : PsiReferenceBase<PropertyAccessMixin>(propertyAccess, textRange, soft) {

  override fun resolve(): PsiElement? = referencedElement

  override fun isReferenceTo(element: PsiElement?): Boolean {
    return referencedElement == element
  }

  override fun getVariants(): Array<Any> = emptyArray()

  override fun getValue(): String {
    return if (referencedElement.text.startsWith("get")) {
      referencedElement.text.substringAfter("get")
          .decapitalize()
    } else {
      referencedElement.text
    }
  }

  override fun handleElementRename(newElementName: String?): PsiElement {
    if (newElementName == null) {
      return propertyAccess
    }

    val actualElement = callChainElement.element as? AccessIdentifierMixin
        ?: return propertyAccess

    val typeDescriptor = callChainElement.type

    actualElement.setName(preprocessName(newElementName, actualElement, typeDescriptor))

    return propertyAccess
  }

  private fun preprocessName(newName: String,
                             actualElement: AccessIdentifierMixin,
                             typeDescriptor: TypeDescriptor): String {
    if (typeDescriptor is JavaPsiClassTypeDescriptor) {
      val psiMember = typeDescriptor.psiMember
      if (psiMember is PsiMethod) {
        return persistNameConventionForMethod(actualElement.variableName(), newName)
      }
    }

    if (typeDescriptor is JavaPsiUnresolvedTypeDescriptor) {
      val psiMember = typeDescriptor.psiMember
      if (psiMember is PsiMethod) {
        return persistNameConventionForMethod(actualElement.variableName(), newName)
      }
    }

    return newName
  }

  private fun persistNameConventionForMethod(oldName: String, newName: String): String =
      when {
        oldName.startsWith("is") && newName.startsWith("is") -> newName
        oldName.startsWith("get") && newName.startsWith("get") -> newName
        newName.startsWith("is") -> newName.substringAfter("is").decapitalize()
        newName.startsWith("get") -> newName.substringAfter("get").decapitalize()
        else -> newName
      }

}
