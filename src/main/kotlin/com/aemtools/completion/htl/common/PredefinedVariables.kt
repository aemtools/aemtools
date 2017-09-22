package com.aemtools.completion.htl.common

import com.aemtools.analysis.htl.callchain.typedescriptor.MergedTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor.Companion.empty
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.predefined.PredefinedTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.properties.PropertiesTypeDescriptor
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.java.JavaSearch
import com.aemtools.service.ServiceFacade
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project

/**
 * Provides completion on Htl context object (e.g. 'properties').
 *
 * @author Dmytro_Troynikov
 */
object PredefinedVariables {

  private val repository = ServiceFacade.getHtlAttributesRepository()

  /**
   * Get all global context objects.
   *
   * @return list of context objects
   */
  fun allContextObjects() = repository.getContextObjects()

  /**
   * Create lookup elements for global context objects.
   *
   * @return list of lookup elements
   */
  fun contextObjectsCompletion(): List<LookupElement> {
    return repository.getContextObjects().map {
      LookupElementBuilder.create(it.name)
          .withTailText("(${it.className})", true)
          .withTypeText("Context Object")
          .withIcon(it.elementIcon)
    }
  }

  /**
   * Create type descriptor for context object by it's identifier.
   *
   * @param variableName context's object identifier
   * @param project the project
   * @return context object's type descriptor
   */
  fun typeDescriptorByIdentifier(variableName: VariableNameMixin, project: Project): TypeDescriptor {
    val name = variableName.variableName()
    val classInfo = repository.findContextObject(name) ?: return TypeDescriptor.empty()
    val originalElement = variableName.originalElement
    val fullClassName = classInfo.className
    val psiClass = JavaSearch.findClass(fullClassName, project)
    val predefined = classInfo.predefined
    return when {
      name == "properties" && psiClass != null && predefined != null && predefined.isNotEmpty() -> {
        MergedTypeDescriptor(
            PropertiesTypeDescriptor(originalElement),
            PredefinedTypeDescriptor(predefined),
            JavaPsiClassTypeDescriptor.create(psiClass)
        )
      }
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

}
