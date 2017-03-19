package com.aemtools.completion.htl.common

import com.aemtools.analysis.htl.callchain.typedescriptor.MergedTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.PredefinedTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.PredefinedVariantsTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor.Companion.empty
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.lang.htl.psi.util.elFields
import com.aemtools.lang.htl.psi.util.elMethods
import com.aemtools.lang.htl.psi.util.elName
import com.aemtools.lang.java.JavaSearch
import com.aemtools.service.ServiceFacade
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.util.containers.isNullOrEmpty
import java.util.*

/**
 * Provides completion on Htl context object (e.g. 'properties')
 * @author Dmytro_Troynikov
 */
object PredefinedVariables {

    private val repository = ServiceFacade.getHtlAttributesRepository()

    fun contextObjectsCompletion(): List<LookupElement> {
        return repository.getContextObjects().map {
            LookupElementBuilder.create(it.name)
                    .withTailText("(${it.className})", true)
                    .withTypeText("Context Object")
                    .withIcon(it.elementIcon)
        }
    }

    /**
     * Lookups for PsiClass by variable name e.g.
     * ```
     *  resolveByIdentifier("request")
     *  ->
     *  "SlingHttpServletRequest"
     * ```
     * @param variableName the name of variable
     * @return [PsiClass] instance or _null_ if no class was found
     */
    fun resolveByIdentifier(variableName: String, project: Project): PsiClass? {
        val classInfo = repository.findContextObject(variableName) ?: return null
        val fullClassName = classInfo.className
        return JavaSearch.findClass(fullClassName, project)
    }

    fun typeDescriptorByIdentifier(variableName: String, project: Project): TypeDescriptor {
        val classInfo = repository.findContextObject(variableName) ?: return TypeDescriptor.empty()
        val fullClassName = classInfo.className
        val psiClass = JavaSearch.findClass(fullClassName, project)
        val predefined = classInfo.predefined
        return when {
            psiClass != null && predefined != null && predefined.isNotEmpty() -> {
                MergedTypeDescriptor(
                        PredefinedTypeDescriptor(predefined),
                        JavaPsiClassTypeDescriptor.create(psiClass)
                )
            }
            psiClass != null -> {
                JavaPsiClassTypeDescriptor.create(psiClass)
            }
            predefined != null && predefined.isNotEmpty() -> {
                PredefinedTypeDescriptor(predefined)
            }
            else -> empty()
        }
    }

    /**
     * Extract Htl applicable completion variants from class element.
     * @return list of LookupElements extracted from given class.
     */
    fun extractSuggestions(psiClass: PsiClass): List<LookupElement> {
        val methods = psiClass.elMethods()
        val fields = psiClass.elFields()

        val methodNames = ArrayList<String>()
        val result = ArrayList<LookupElement>()

        methods.forEach {
            var name = it.elName()
            if (methodNames.contains(name)) {
                name = it.name
            } else {
                methodNames.add(name)
            }
            var lookupElement = LookupElementBuilder.create(name)
                    .withIcon(it.getIcon(0))
                    .withTailText(" ${it.name}()", true)

            val returnType = it.returnType
            if (returnType != null) {
                lookupElement = lookupElement.withTypeText(returnType.presentableText, true)
            }

            result.add(lookupElement)
        }

        fields.forEach {
            val lookupElement = LookupElementBuilder.create(it.name.toString())
                    .withIcon(it.getIcon(0))
                    .withTypeText(it.type.presentableText, true)

            result.add(lookupElement)
        }

        return result
    }

}