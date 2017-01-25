package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.MapTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.lang.java.JavaSearch
import com.intellij.psi.PsiClass
import com.intellij.psi.impl.source.PsiClassReferenceType

/**
 * @author Dmytro Troynikov
 */
class MapJavaTypeDescriptor(psiClass: PsiClass,
                            override val originalType: PsiClassReferenceType? = null) :
        JavaPsiClassTypeDescriptor(psiClass, originalType), MapTypeDescriptor {
    override fun keyType(): TypeDescriptor {
        if (originalType == null) {
            return TypeDescriptor.empty()
        }

        val keyParam = originalType.parameters[0].canonicalText

        val psiClass = JavaSearch.findClass(keyParam, psiClass.project)
                ?: return TypeDescriptor.empty()

        return JavaPsiClassTypeDescriptor.create(psiClass, null)
    }

    override fun valueType(): TypeDescriptor {
        if (originalType == null) {
            return TypeDescriptor.empty()
        }

        val valueParam = originalType.parameters[1].canonicalText

        val psiClass = JavaSearch.findClass(valueParam, psiClass.project)
                ?: return TypeDescriptor.empty()

        return JavaPsiClassTypeDescriptor.create(psiClass, null)
    }

}