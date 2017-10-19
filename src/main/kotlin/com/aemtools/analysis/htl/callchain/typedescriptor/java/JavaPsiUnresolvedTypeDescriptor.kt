package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.base.EmptyTypeDescriptor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMember

/**
 * @author Dmytro Troynikov
 */
open class JavaPsiUnresolvedTypeDescriptor(
    val psiMember: PsiMember
) : EmptyTypeDescriptor() {

  override fun referencedElement(): PsiElement? = psiMember

}
