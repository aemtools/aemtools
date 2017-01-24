package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.IterableTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.lang.java.JavaSearch
import com.intellij.psi.PsiClass
import com.intellij.psi.impl.source.PsiClassReferenceType

/**
 * @author Dmytro Troynikov
 */
class IterableJavaTypeDescriptor(psiClass: PsiClass,
                                 override val originalType: PsiClassReferenceType? = null)
    : JavaPsiClassTypeDescriptor(psiClass, originalType), IterableTypeDescriptor {
    override fun iterableType(): TypeDescriptor {
        val className = originalType?.parameters?.get(0)?.canonicalText
                ?: return TypeDescriptor.empty()

        val psiClass = JavaSearch.findClass(className, psiClass.project)
                ?: return TypeDescriptor.empty()

        return JavaPsiClassTypeDescriptor.create(psiClass, null)
    }

}