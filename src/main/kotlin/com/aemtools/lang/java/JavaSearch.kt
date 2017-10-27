package com.aemtools.lang.java

import com.aemtools.constant.const.java.POJO_USE
import com.aemtools.constant.const.java.USE_INTERFACE
import com.aemtools.constant.const.java.WCM_USE_CLASS
import com.aemtools.service.IJavaSearchService
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.psi.search.searches.ClassInheritorsSearch

/**
 * Utility object for java search.
 *
 * @author Dmytro Troynikov
 */
object JavaSearch {

  val USE_CLASSES = listOf(USE_INTERFACE, WCM_USE_CLASS, POJO_USE)

  /**
   * Search for [PsiClass] by qualified name with predefined "allScope".
   *
   * Shortcut for [JavaPsiFacade.findClass]
   * @param qualifiedName the full qualified name of class
   * @param project the project
   * @see JavaPsiFacade
   * @return [PsiClass] instance, __null__ if no instance was found
   */
  fun findClass(qualifiedName: String, project: Project)
      : PsiClass? =
      service()
          ?.findClass(qualifiedName, project)

  /**
   * Search for inheritors of given [PsiClass].
   *
   * Shortcut for [ClassInheritorsSearch.search]
   * @see ClassInheritorsSearch
   * @param psiClass the base class
   * @param project the project
   * @return list of inheritors of given class
   */
  fun findInheritors(psiClass: PsiClass, project: Project): List<PsiClass> =
      service()
          ?.findInheritors(psiClass, project)
          ?: emptyList()

  /**
   * Search classes annotated by given annotation.
   *
   * Shortcut for [AnnotatedElementsSearch.searchPsiClasses]
   * @see AnnotatedElementsSearch
   * @param annotation the type of annotation
   * @param project the project
   * @return list of annotated classes
   */
  fun findAnnotatedClasses(annotation: PsiClass, project: Project): List<PsiClass> =
      service()
          ?.findAnnotatedClasses(annotation, project)
          ?: emptyList()

  /**
   * Find all sling models in the project.
   * @param project the project
   * @return list of sling models
   */
  fun findSlingModels(project: Project): List<PsiClass> =
      service()
          ?.findSlingModels(project)
          ?: emptyList()

  /**
   * Find all __io.sightly.java.api.Use__ and __com.adobe.cq.sightly.WCMUse__
   * inheritors in given project.
   * @param project the project
   * @return list of inheritors
   */
  fun findWcmUseClasses(project: Project): List<PsiClass> =
      service()
          ?.findWcmUseClasses(project)
          ?: emptyList()

  private fun service(): IJavaSearchService? = ServiceManager.getService(IJavaSearchService::class.java)

}
