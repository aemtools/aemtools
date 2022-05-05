package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.base.MapTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.common.util.toPsiClass
import com.aemtools.lang.java.JavaSearch
import com.aemtools.lang.java.JavaUtilities
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMember
import com.intellij.psi.impl.source.PsiClassReferenceType

/**
 * @author Dmytro Primshyts
 */
class MapJavaTypeDescriptor(psiClass: PsiClass,
                            psiMember: PsiMember?,
                            override val originalType: PsiClassReferenceType? = null) :
    JavaPsiClassTypeDescriptor(psiClass, psiMember, originalType), MapTypeDescriptor {
  override fun keyType(): TypeDescriptor {
    if (originalType == null) {
      return TypeDescriptor.empty()
    }

    val keyParam = getMapType()?.parameters?.get(0)?.canonicalText ?: return TypeDescriptor.empty()

    val psiClass = JavaSearch.findClass(keyParam, psiClass.project)
        ?: return TypeDescriptor.empty()

    return JavaPsiClassTypeDescriptor.create(psiClass, null, null)
  }

  override fun valueType(): TypeDescriptor {
    if (originalType == null) {
      return TypeDescriptor.empty()
    }

    val valueType = getMapType()?.parameters?.get(1)?.canonicalText ?: return TypeDescriptor.empty()

    val psiClass = JavaSearch.findClass(valueType, psiClass.project)
        ?: return TypeDescriptor.empty()

    return JavaPsiClassTypeDescriptor.create(psiClass, null, null)
  }

  private fun getMapType(): PsiClassReferenceType? =
      if (originalType?.parameterCount == 0) {
        getMapSupertype()
      } else {
        originalType
      }

  private fun getMapSupertype(): PsiClassReferenceType? {
    return originalType
        ?.superTypes
        ?.find {
          val psiClass = it.toPsiClass() ?: return@find false
          JavaUtilities.isMap(psiClass)
        } as? PsiClassReferenceType
  }
}
