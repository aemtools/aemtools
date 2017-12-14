package com.aemtools.inspection.service

import com.aemtools.lang.htl.psi.HtlHtlEl
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.module.Module
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
interface IInspectionService {

  /**
   * Check if given psi element is valid target for inspection.
   *
   * @param psiElement element to inspect
   * @return *true* if given element is valid element for inspection,
   * *false* otherwise
   */
  fun validTarget(psiElement: PsiElement): Boolean

  /**
   * Register "messed data-sly-attribute" problem.
   *
   * @param holder the problem holder
   * @param attribute the "problematic" xml attribute
   * @param variableName the name of wrong attribute
   */
  fun messedDataSlyAttribute(holder: ProblemsHolder, attribute: XmlAttribute, variableName: String)

  /**
   * Register "redundant data-sly-unwrap" problem.
   *
   * @param holder the problem holder
   * @param attribute the "problematic" xml attribute
   */
  fun redundantDataSlyUnwrap(holder: ProblemsHolder, attribute: XmlAttribute)


  /**
   * Find [Module] for given [PsiElement].
   *
   * @param psiElement element to find module against
   * @return the module
   */
  fun moduleForPsiElement(psiElement: PsiElement): Module?

  /**
   * Report redundant EL.
   *
   * @param element element - holder of redundant expression
   * @param problemsHolder the problems holder
   */
  fun reportRedundantEl(element: HtlHtlEl, problemsHolder: ProblemsHolder)


}
