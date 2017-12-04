package com.aemtools.lang.java

import com.aemtools.constant.const
import com.intellij.psi.PsiClass

/**
 * @author Dmytro Troynikov
 */
object JavaUtilities {

  /**
   * Check if given class is [java.lang.Iterable].
   *
   * @param psiClass class to check
   * @return *true* if given class implements iterable interface,
   * *false* otherwise
   */
  fun isIterable(psiClass: PsiClass): Boolean
      = isInheritorOf(psiClass, const.java.ITERABLE)

  /**
   * Check if given class is [java.util.Iterator].
   *
   * @param psiClass class to check
   * @return *true* if given class implements iterator interface,
   * *false* otherwise
   */
  fun isIterator(psiClass: PsiClass): Boolean
      = isInheritorOf(psiClass, const.java.ITERATOR)

  /**
   * Check if given class is [java.util.Map].
   *
   * @param psiClass class to check
   * @return *true* if given class implements map interface,
   * *false* otherwise
   */
  fun isMap(psiClass: PsiClass): Boolean
      = isInheritorOf(psiClass, const.java.MAP)

  private fun isInheritorOf(psiClass: PsiClass, other: String): Boolean {
    val otherClass = JavaSearch.findClass(other, psiClass.project)
        ?: return false
    return psiClass.isEquivalentTo(otherClass)
        || psiClass.isInheritor(otherClass, true)
  }

}
