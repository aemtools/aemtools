package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.MapTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.intellij.psi.PsiClass

/**
 * @author Dmytro Troynikov
 */
class MapJavaTypeDescriptor(psiClass: PsiClass) :
        JavaPsiClassTypeDescriptor(psiClass), MapTypeDescriptor {
    override fun keyType(): TypeDescriptor {
        throw UnsupportedOperationException("not implemented")
    }

    override fun valueType(): TypeDescriptor {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}