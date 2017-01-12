package com.aemtools.documentation.htl

import com.aemtools.completion.util.isHtlAttribute
import com.aemtools.constant.const.htl.HTL_ATTRIBUTES
import com.aemtools.documentation.BaseDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute

/**
 * TODO: fill documentation values
 * @author Dmytro_Troynikov
 */
open class HtlAttributesDocumentationProvider : BaseDocumentationProvider() {

    override fun acceptGenerateDoc(element: PsiElement): Boolean {
        val file = element.containingFile

        return false
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        val attribute = element as? XmlAttribute ?: return null

        if (!attribute.isHtlAttribute()) {
            return null
        }

        return HtlAttributesDocumentation.documentation
                .entries
                .find {
                    attribute.name.startsWith(it.key)
                }
                ?.value ?: return null
    }

}

object HtlAttributesDocumentation {
    val documentation: Map<String, String> = HTL_ATTRIBUTES.map {
        it to "Documentation for $it will be available soon"
    }.toMap()
}