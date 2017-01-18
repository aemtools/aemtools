package com.aemtools.lang.java

import com.aemtools.constant.const
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.psi.search.searches.ClassInheritorsSearch

/**
 * Utility object for java search.
 * @author Dmytro Troynikov
 */
object JavaSearch {

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
            JavaPsiFacade.getInstance(project)
                    .findClass(qualifiedName, searchScope(project))

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
            ClassInheritorsSearch.search(psiClass, searchScope(project), true)
                    .findAll().toList()

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
        AnnotatedElementsSearch.searchPsiClasses(annotation, searchScope(project))
                .findAll().toList()

    /**
     * Find all sling models in the project.
     * @param project the project
     * @return list of sling models
     */
    fun findSlingModels(project: Project): List<PsiClass> =
        findClass(const.java.SLING_MODEL, project).let {
            if (it != null) {
                findAnnotatedClasses(it, project)
            } else {
                listOf()
            }
        }

    /**
     * Find all __io.sightly.java.api.Use__ and __com.adobe.cq.sightly.WCMUse__
     * inheritors in given project.
     * @param project the project
     * @return list of inheritors
     */
    fun findWcmUseClasses(project: Project): List<PsiClass> {
        val useInheritors = findClass(const.java.USE_INTERFACE, project)
                .let {
                    if (it != null) {
                        findInheritors(it, project)
                    } else {
                        listOf()
                    }
                }

        val wcmUseInheritors = findClass(const.java.WCM_USE_CLASS, project)
                .let {
                    if (it != null) {
                        findInheritors(it, project)
                    } else {
                        listOf()
                    }
                }
        return (useInheritors + wcmUseInheritors).toSet().toList()
    }

    private fun searchScope(project: Project) = GlobalSearchScope.allScope(project)

}