package com.aemtools.completion.htl.completionprovider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.psi.search.searches.ClassInheritorsSearch
import com.intellij.util.ProcessingContext

/**
 * Code completion for __data-sly-use.*__ attribute. (e.g. <div data-sly-use.bean="<caret>")
 * Collects all classes in project which do implement
 * @author Dmytro Troynikov.
 */
object SlyUseCompletionProvider : CompletionProvider<CompletionParameters>() {

    val USE_INTERFACE = "io.sightly.java.api.Use"
    val WCM_USE_CLASS = "com.adobe.cq.sightly.WCMUse"
    val SLING_MODEL = "org.apache.sling.models.annotations.Model"

    fun useSuggestions(parameters: CompletionParameters): List<LookupElement> {
        val useClasses = findWcmUseClasses(parameters)
        val slingModelClasses = findSlingModels(parameters)
        val classes = useClasses + slingModelClasses

        return classes
                .filter { !it.hasModifierProperty(PsiModifier.ABSTRACT) }
                .map {
                    LookupElementBuilder.create(it.qualifiedName as String)
                            .withLookupString(it.name as String)
                }
    }

    override fun addCompletions(parameters: CompletionParameters,
                                context: ProcessingContext?,
                                result: CompletionResultSet) {
        result.addAllElements(useSuggestions(parameters))
    }

    private fun findWcmUseClasses(parameters: CompletionParameters): List<PsiClass> {
        val project = parameters.editor.project ?: return listOf()
        val scope = GlobalSearchScope.everythingScope(project)

        val javaPsiFacade = JavaPsiFacade.getInstance(project)

        val useInterface = javaPsiFacade
                .findClass(USE_INTERFACE, scope)

        val wcmUseClass = javaPsiFacade
                .findClass(WCM_USE_CLASS, scope)


        val useClasses: List<PsiClass> = if (useInterface != null) {
            ClassInheritorsSearch.search(useInterface, scope, true)
                    .findAll().toList()
        } else {
            listOf()
        }

        val wcmUseClasses: List<PsiClass> = if (wcmUseClass != null) {
            ClassInheritorsSearch.search(wcmUseClass, scope, true)
                    .findAll().toList()
        } else {
            listOf()
        }

        return (useClasses + wcmUseClasses).toSet().toList()
    }

    private fun findSlingModels(parameters: CompletionParameters): List<PsiClass> {
        val project = parameters.editor.project ?: return listOf()
        val scope = GlobalSearchScope.everythingScope(project)

        val slingModelAnnotation = JavaPsiFacade
                .getInstance(project)
                .findClass(SLING_MODEL, scope)
                ?: return listOf()

        return AnnotatedElementsSearch.searchPsiClasses(slingModelAnnotation, scope)
                .findAll().toList()
    }

}