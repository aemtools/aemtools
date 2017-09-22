package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.base.IterableTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.lang.java.JavaSearch
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMember
import com.intellij.psi.impl.source.PsiClassReferenceType

/**
 * @author Dmytro Troynikov
 */
class IterableJavaTypeDescriptor(psiClass: PsiClass,
                                 psiMember: PsiMember?,
                                 override val originalType: PsiClassReferenceType? = null)
  : JavaPsiClassTypeDescriptor(psiClass, psiMember, originalType), IterableTypeDescriptor {
  override fun iterableType(): TypeDescriptor {
    val className = originalType?.parameters?.getOrNull(0)?.canonicalText
        ?: return TypeDescriptor.empty()

    val psiClass = JavaSearch.findClass(className, psiClass.project)
        ?: return TypeDescriptor.empty()

    return JavaPsiClassTypeDescriptor.create(psiClass, null, null)
  }

}
