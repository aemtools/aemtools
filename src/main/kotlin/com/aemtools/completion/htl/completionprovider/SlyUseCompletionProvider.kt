package com.aemtools.completion.htl.completionprovider

import com.aemtools.index.HtlTemplateIndex
import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext
import com.intellij.util.indexing.FileBasedIndex

/**
 * Code completion for __data-sly-use.*__ attribute. (e.g. <div data-sly-use.bean="<caret>")
 * Collects all classes in project which do implement
 * @author Dmytro Troynikov.
 */
object SlyUseCompletionProvider : CompletionProvider<CompletionParameters>() {

    fun useSuggestions(parameters: CompletionParameters): List<LookupElement> {
        val project = parameters.position.project

        val useClasses = JavaSearch.findWcmUseClasses(project)
        val slingModelClasses = JavaSearch.findSlingModels(project)

        val useClassesVariants = extractCompletions(useClasses, "Use Class")
        val slingModelVariants = extractCompletions(slingModelClasses, "Sling Model")
        val templates = extractTemplates(parameters)

        return useClassesVariants + slingModelVariants + templates
    }

    private fun extractCompletions(classes: List<PsiClass>, type: String): List<LookupElement> {
        return classes.filter { !it.hasModifierProperty(PsiModifier.ABSTRACT) }
                .map {
                    val qualifiedName = it.qualifiedName as String
                    val name = it.name as String
                    val result = LookupElementBuilder.create(qualifiedName)
                            .withLookupString(name)
                            .withPresentableText(name)
                            .withIcon(it.getIcon(0))
                            .withTypeText(type)
                            .withTailText("(${qualifiedName.substring(0, qualifiedName.lastIndexOf("."))})", true)
                    result
                }
    }

    override fun addCompletions(parameters: CompletionParameters,
                                context: ProcessingContext?,
                                result: CompletionResultSet) {
        result.addAllElements(useSuggestions(parameters))
    }

    private fun extractTemplates(parameters: CompletionParameters): List<LookupElement> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(HtlTemplateIndex.HTL_TEMPLATE_ID, parameters.position.project)
        val result = keys.flatMap {
            fbi.getValues(HtlTemplateIndex.HTL_TEMPLATE_ID, it, GlobalSearchScope.allScope(parameters.position.project))
        }

        return result.map {
            LookupElementBuilder.create(it.normalizedPath)
                    .withTypeText("HTL Template")
                    .withTailText("(${it.normalizedPath})", true)
                    .withPresentableText(it.fileName)
                    .withIcon(AllIcons.FileTypes.Html)
        }
    }

}