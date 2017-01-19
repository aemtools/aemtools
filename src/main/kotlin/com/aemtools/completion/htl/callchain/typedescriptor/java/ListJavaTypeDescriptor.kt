package com.aemtools.completion.htl.callchain.typedescriptor.java

import com.aemtools.completion.htl.callchain.typedescriptor.ListTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.TypeDescriptor
import com.intellij.psi.PsiClass

/**
 * @author Dmytro Troynikov
 */
class ListJavaTypeDescriptor(psiClass: PsiClass)
    : JavaPsiClassTypeDescriptor(psiClass), ListTypeDescriptor {
    override fun listType(): TypeDescriptor {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}