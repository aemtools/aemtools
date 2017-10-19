package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.base.ArrayTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiType

/**
 * @author Dmytro Troynikov
 */
class ArrayJavaTypeDescriptor(psiClass: PsiClass,
                              psiMember: PsiMember?,
                              originalType: PsiType? = null)
  : JavaPsiClassTypeDescriptor(psiClass, psiMember, originalType), ArrayTypeDescriptor {

  override fun isArray() = true

  override fun isIterable() = false

  override fun isMap() = false

  //for array the 'arrayType' is the same as main type
  override fun arrayType(): TypeDescriptor = this

}
