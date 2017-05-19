package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.completion.util.resourceType
import com.aemtools.index.search.AemComponentSearch
import com.aemtools.util.withPriority
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement

/**
 * @autor Dmytro Troynikov
 */
class PropertiesTypeDescriptor(val element: PsiElement) : TypeDescriptor {
    override fun myVariants(): List<LookupElement> {
        val myResourceType = element.containingFile.originalFile.virtualFile.resourceType()
                ?: return emptyList()

        val touchUiDialog = AemComponentSearch.findTouchUIDialogByResourceType(myResourceType, element.project)
        if (touchUiDialog != null) {
            return touchUiDialog.myParameters.map {
                it.toLookupElement()
                        .withPriority(1.toDouble())
            }
        }

        val classicDialog = AemComponentSearch.findClassicDialogByResourceType(myResourceType, element.project)
        if (classicDialog != null) {
            return classicDialog.myParameters.map {
                it.toLookupElement()
                        .withPriority(1.toDouble())
            }
        }
        return emptyList()
    }


    override fun subtype(identifier: String): TypeDescriptor =
            EmptyTypeDescriptor()

    override fun name(): String = "properties"

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false

    override fun isMap(): Boolean = true

}