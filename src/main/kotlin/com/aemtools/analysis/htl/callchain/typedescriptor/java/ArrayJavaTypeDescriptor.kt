package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.ArrayTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiType

/**
 * @author Dmytro Troynikov
 */
class ArrayJavaTypeDescriptor(psiClass: PsiClass,
                              originalType: PsiType? = null)
    : JavaPsiClassTypeDescriptor(psiClass, originalType), ArrayTypeDescriptor {

    override fun isArray() = true

    override fun isIterable() = false

    override fun isMap() = false

    //for array the 'arrayType' is the same as main type
    override fun arrayType(): TypeDescriptor = this

}