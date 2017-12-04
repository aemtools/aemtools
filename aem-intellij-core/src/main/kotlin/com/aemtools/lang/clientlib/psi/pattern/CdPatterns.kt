package com.aemtools.lang.clientlib.psi.pattern

import com.aemtools.lang.clientlib.psi.CdTypes
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement

/**
 * @author Dmytro_Troynikov
 */
object CdPatterns {

  /**
   * Will match:
   *
   * ```
   * #base=<caret>
   * ```
   */
  val basePath: ElementPattern<PsiElement> =
      psiElement().inside(psiElement(CdTypes.INCLUDE).inside(psiElement(CdTypes.BASE_PATH)))

  /**
   * Will match;
   * ```
   * <caret>
   * ```
   */
  val include: ElementPattern<PsiElement> =
      psiElement().inside(psiElement(CdTypes.INCLUDE))

}
