package com.aemtools.test.fixture

import com.aemtools.lang.java.JavaSearch
import com.aemtools.test.base.BaseLightTest
import com.intellij.psi.PsiClass

/**
 * [JavaSearch] utilities.
 *
 * @author Dmytro Primshyts
 */
interface JavaSearchMixin {

  /**
   * Get [PsiClass] by full qualified name.
   *
   * @param fqn full qualified class name
   * @receiver [BaseLightTest]
   * @throws AssertionError in case if given class not found
   */
  fun BaseLightTest.psiClass(fqn: String): PsiClass = (JavaSearch.findClass(fqn, project)
      ?: throw AssertionError("Unable to find fixture class: $fqn"))

}
