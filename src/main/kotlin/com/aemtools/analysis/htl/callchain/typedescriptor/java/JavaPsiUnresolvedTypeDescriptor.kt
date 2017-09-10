package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.base.EmptyTypeDescriptor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiType

/**
 * @author Dmytro Troynikov
 */
open class JavaPsiUnresolvedTypeDescriptor(private val myName: String,
                                           val psiMember: PsiMember,
                                           val psiType: PsiType) : EmptyTypeDescriptor() {

    override fun name(): String = myName

    override fun referencedElement(): PsiElement? = psiMember

}
