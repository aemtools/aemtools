package com.aemtools.completion.htl.completionprovider

import com.aemtools.completion.htl.model.PropertyAccessChainUnit
import com.aemtools.service.ServiceFacade
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiClass
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov
 */
object PredefinedEL {

    private val repository = ServiceFacade.getHtlAttributesRepository()

    fun predefinedCompletions(parameters: CompletionParameters,
                              context: ProcessingContext,
                              result: CompletionResultSet): Pair<List<LookupElement>, String> {
        val classChain = context.get("property-access-chain") as List<PropertyAccessChainUnit>?
                ?: return listOf<LookupElement>() to ""

        val lastElement = classChain.last()

        val predefinedClass = repository.getContextObjects()
                .find {
                    val classElement = lastElement.resolutionResult.psiClass as? PsiClass
                    if (classElement == null) {
                        false
                    } else {
                        it.className == classElement.qualifiedName && it.name == lastElement.variableName
                    }
                } ?: return listOf<LookupElement>() to ""

        if (predefinedClass.predefined == null) {
            return listOf<LookupElement>() to ""
        }

        return predefinedClass.predefined.map {
            LookupElementBuilder.create(it.completionText)
        } to predefinedClass.predefinedMode
    }

}