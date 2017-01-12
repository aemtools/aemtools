package com.aemtools.documentation

import com.aemtools.documentation.htl.HtlAttributesDocumentationProvider
import com.aemtools.documentation.htl.el.HtlELDocumentationProvider
import com.aemtools.documentation.widget.WidgetDocumentationProvider
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement

/**
 * DocumentationProvider entry point.
 * @author Dmytro_Troynikov
 */
object DocumentationProvider : AbstractDocumentationProvider() {

    val providers = listOf<BaseDocumentationProvider>(
            HtlELDocumentationProvider(),
            HtlAttributesDocumentationProvider(),
            WidgetDocumentationProvider())

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (originalElement != null) {
            val provider = providers.find { it.acceptGenerateDoc(originalElement)} ?: return null

            return provider.generateDoc(element, originalElement)
        }

        return null
    }

}