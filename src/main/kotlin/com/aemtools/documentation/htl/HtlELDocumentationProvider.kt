package com.aemtools.documentation.htl

import com.aemtools.completion.util.findParentByType
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.contextOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.memberAccess
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.optionName
import com.aemtools.service.repository.inmemory.HtlAttributesRepository
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import generated.psi.impl.HtlStringLiteralImpl

/**
 *
 * @author Dmytro Troynikov.
 */
open class HtlELDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        val text = originalElement?.text ?: return super.generateDoc(element, originalElement)
        return when {
            optionName.accepts(originalElement) -> {
                HtlAttributesRepository.getHtlOptions().find {
                    it.name == text
                }?.let(HtlAttributesRepository.HtlOption::description)
                        ?: super.generateDoc(element, originalElement)

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
            memberAccess.accepts(originalElement) -> {
                super.generateDoc(element, originalElement)
                val propertyAccessMixin = originalElement.findParentByType(PropertyAccessMixin::class.java)
                    ?: return super.generateDoc(element, originalElement)
                val variableNameMixin = originalElement.findParentByType(VariableNameMixin::class.java)
                    ?: return super.generateDoc(element, originalElement)
                val currentChainElement = propertyAccessMixin.accessChain()?.findChainElement(variableNameMixin)
                    ?: return super.generateDoc(element, originalElement)

                currentChainElement.type.documentation()
                    ?: super.generateDoc(element, originalElement)
            }
            else -> super.generateDoc(element, originalElement)
        }
    }

}