package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.IterableTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.intellij.psi.PsiClass

/**
 * @author Dmytro Troynikov
 */
class ListJavaTypeDescriptor(psiClass: PsiClass)
    : JavaPsiClassTypeDescriptor(psiClass), IterableTypeDescriptor {
    override fun iterableType(): TypeDescriptor {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}