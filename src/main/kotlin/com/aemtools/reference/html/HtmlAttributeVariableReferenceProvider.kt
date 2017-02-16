package com.aemtools.reference.html

import com.aemtools.analysis.htl.callchain.elements.BaseCallChainSegment
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.getHtlFile
import com.aemtools.completion.util.isHtlAttribute
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov
 */
object HtmlAttributeVariableReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
        val attribute = element as XmlAttribute

        if (attribute.isHtlAttribute() && attribute.name.contains(".")) {
            val variableName = attribute.name.substring(attribute.name.indexOf(".") + 1)

            if (variableName.isNotEmpty()) {
                val htlPsi = attribute.containingFile.getHtlFile()
                        ?: return arrayOf()

                val els = htlPsi.findChildrenByClass(HtlHtlEl::class.java).flatMap {
                    it.findChildrenByType(PropertyAccessMixin::class.java)
                }

                val filtered = els.flatMap {
                    it.accessChain()?.let { listOf(it) }
                            .orEmpty()
                }.filter {
                    val lastSegment = it.callChainSegments.lastOrNull()
                            ?: return@filter false

                    false

                    it.callChainSegments.forEach {
                        if (it is BaseCallChainSegment
                             && it.declaration?.xmlAttribute?.isEquivalentTo(attribute)
                                ?: false){
                            return@filter true

                        }
                    }
                    return@filter false
                }
            }
        }
        return arrayOf()
    }

}