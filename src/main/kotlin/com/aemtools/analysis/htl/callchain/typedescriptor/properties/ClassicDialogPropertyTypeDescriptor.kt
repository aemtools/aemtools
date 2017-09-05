package com.aemtools.analysis.htl.callchain.typedescriptor.properties

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.index.model.dialog.AemComponentClassicDialogDefinition
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
class ClassicDialogPropertyTypeDescriptor(
        val name: String,
        val element: PsiElement,
        val classicDialogDefinition: AemComponentClassicDialogDefinition)
    : TypeDescriptor {

    override fun referencedElement(): PsiElement? =
            classicDialogDefinition.declarationElement(name, element.project)

    override fun myVariants(): List<LookupElement> = emptyList()
    override fun subtype(identifier: String): TypeDescriptor
            = TypeDescriptor.empty()

    override fun name(): String = name

    override fun isArray(): Boolean = false
    override fun isIterable(): Boolean = false
    override fun isMap(): Boolean = false
}
