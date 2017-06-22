package com.aemtools.reference.html.provider

import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isDataSlyUse
import com.aemtools.index.HtlIndexFacade
import com.aemtools.lang.java.JavaSearch
import com.aemtools.reference.common.reference.PsiFileReference
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * Adds references in data-sly-use attributes.
 *
 * @author Dmytro_Troynikov
 */
object HtmlDataSlyUseWithinAttributeValueReferenceProvider : JavaClassReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val attr = element.findParentByType(XmlAttribute::class.java) ?: return arrayOf()
        val valueElement = attr.valueElement ?: return arrayOf()
        if (attr.isDataSlyUse()) {
            val psiFile = HtlIndexFacade.resolveFile(valueElement.value, attr.containingFile)

            if (psiFile != null) {
                val fileReference = PsiFileReference(psiFile,
                        valueElement,
                        TextRange(1, valueElement.textLength - 1))
                return arrayOf(fileReference)
            }

            val psiClass = JavaSearch.findClass(valueElement.value, element.project)
                    ?: return arrayOf()

            return getReferencesByString(psiClass.qualifiedName, element, 1)
        }
        return arrayOf()
    }

    /**
     * Intentionally using global scope here, since Model classes can be defined in another module, which is not explicitly
     * declared as a dependency for current module, resulting in non-resolvable reference.
     * @see com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReference.getScope
     */
    override fun getScope(project: Project?): GlobalSearchScope? {
        project ?: return null
        return GlobalSearchScope.allScope(project)
    }
}