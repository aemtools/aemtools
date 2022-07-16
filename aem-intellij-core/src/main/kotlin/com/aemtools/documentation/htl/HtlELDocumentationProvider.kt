package com.aemtools.documentation.htl

import com.aemtools.analysis.htl.callchain
import com.aemtools.common.util.findParentByType
import com.aemtools.completion.model.htl.HtlOption
import com.aemtools.documentation.clientlibs.generator.ClientlibDocumentationGenerator
import com.aemtools.index.model.AemComponentDefinition.Companion.generateDoc
import com.aemtools.index.search.AemComponentSearch
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.categoriesOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.categoriesOptionAssignmentViaArray
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.contextOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.memberAccess
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.optionName
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.resourceTypeOptionAssignment
import com.aemtools.lang.settings.model.HtlVersion
import com.aemtools.lang.util.getHtlVersion
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import generated.psi.impl.HtlStringLiteralImpl

/**
 * Main documentation provider for [HtlLanguage].
 *
 * @author Dmytro Primshyts
 */
open class HtlELDocumentationProvider : AbstractDocumentationProvider() {

  override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
    val text = originalElement?.text ?: return super.generateDoc(element, originalElement)
    return when {
      optionName.accepts(originalElement) -> {
        getAllHtlOptions(originalElement.project.getHtlVersion()).find {
          it.name == text
        }?.let(HtlOption::description)
            ?: super.generateDoc(element, originalElement)
      }

      resourceTypeOptionAssignment.accepts(originalElement) -> {
        val resourceType = (originalElement.findParentByType(HtlStringLiteralImpl::class.java))?.name
            ?: return super.generateDoc(element, originalElement)

        val component = AemComponentSearch.findByResourceType(resourceType, originalElement.project)
            ?: return super.generateDoc(element, originalElement)

        return component.generateDoc()
      }

      contextOptionAssignment.accepts(originalElement) -> {
        val literal = originalElement.findParentByType(HtlStringLiteralImpl::class.java)
            ?: return super.generateDoc(element, originalElement)
        val stringValue = literal.name

        HtlAttributesRepository.getContextValues().find {
          it.name == stringValue
        }?.let(HtlAttributesRepository.HtlContextValue::description)
            ?: super.generateDoc(element, originalElement)
      }

      categoriesOptionAssignment.accepts(originalElement) ||
          categoriesOptionAssignmentViaArray.accepts(originalElement) -> {
        val category = originalElement.findParentByType(HtlStringLiteralImpl::class.java)
            ?.name ?: return super.generateDoc(element, originalElement)

        if (category.isEmpty()) {
          return super.generateDoc(element, originalElement)
        }

        ClientlibDocumentationGenerator.generateDoc(originalElement, category)
            ?: super.generateDoc(element, originalElement)
      }

      memberAccess.accepts(originalElement) -> {
        val propertyAccessMixin = originalElement.findParentByType(PropertyAccessMixin::class.java)
            ?: return super.generateDoc(element, originalElement)
        val variableNameMixin = originalElement.findParentByType(VariableNameMixin::class.java)
            ?: return super.generateDoc(element, originalElement)
        val currentChainElement = propertyAccessMixin.callchain()?.findChainElement(variableNameMixin)
            ?: return super.generateDoc(element, originalElement)

        currentChainElement.type.documentation()
            ?: super.generateDoc(element, originalElement)
      }
      else -> super.generateDoc(element, originalElement)
    }
  }

  private fun getAllHtlOptions(htlVersion: HtlVersion): List<HtlOption> =
      HtlAttributesRepository.getAttributesData(htlVersion).flatMap {
        it.options ?: listOf()
      } + HtlAttributesRepository.getHtlOptions(htlVersion)

}
