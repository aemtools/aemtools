package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.ArrayTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.intellij.psi.PsiClass
import java.lang.reflect.Type

/**
 * @author Dmytro Troynikov
 */
class ArrayJavaTypeDescriptor(psiClass: PsiClass)
    : JavaPsiClassTypeDescriptor(psiClass), ArrayTypeDescriptor {

    override fun isArray() = true

    override fun isIterable() = false

    override fun isMap() = false

    //for array the 'arrayType' is the same as main type
    override fun arrayType(): TypeDescriptor = this

}