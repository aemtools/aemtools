package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.completion.htl.CompletionPriority.DIALOG_PROPERTY
import com.aemtools.completion.util.resourceType
import com.aemtools.index.model.dialog.AemComponentClassicDialogDefinition
import com.aemtools.index.model.dialog.AemComponentTouchUIDialogDefinition
import com.aemtools.index.search.AemComponentSearch
import com.aemtools.util.withPriority
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiElement

/**
 * Type descriptor for `properties` context object.
 *
 * @author Dmytro Troynikov
 */
class PropertiesTypeDescriptor(val element: PsiElement) : TypeDescriptor {

    private val myResourceType: String? by lazy {
        element.containingFile.originalFile.virtualFile.resourceType()
    }

    private val touchUIDialog: AemComponentTouchUIDialogDefinition? by lazy {
        myResourceType?.let {
            AemComponentSearch.findTouchUIDialogByResourceType(it, element.project)
        }
    }

    private val classicDialog: AemComponentClassicDialogDefinition? by lazy {
        myResourceType?.let {
            AemComponentSearch.findClassicDialogByResourceType(it, element.project)
        }
    }

    override fun myVariants(): List<LookupElement> {
        touchUIDialog?.let {
            return it.myParameters.map {
                it.toLookupElement()
                        .withPriority(DIALOG_PROPERTY)
            }
        }
        classicDialog?.let {
            return it.myParameters.map {
                it.toLookupElement()
                        .withPriority(DIALOG_PROPERTY)
            }
        }
        return emptyList()
    }

    override fun subtype(identifier: String): TypeDescriptor {
        touchUIDialog?.let {
            return TouchDialogPropertyTypeDescriptor(
                    identifier,
                    element,
                    it)
        }
        classicDialog?.let {
            return ClassicDialogPropertyTypeDescriptor(
                    identifier,
                    element,
                    it
            )
        }
        return TypeDescriptor.empty()
    }

    override fun name(): String = "properties"

    override fun isArray(): Boolean = false

    override fun isIterable(): Boolean = false

    override fun isMap(): Boolean = true

}

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

class TouchDialogPropertyTypeDescriptor(
        val name: String,
        val element: PsiElement,
        val touchDialogDefinition: AemComponentTouchUIDialogDefinition)
    : TypeDescriptor {

    override fun referencedElement(): PsiElement? =
            touchDialogDefinition.declarationElement(name, element.project)

    override fun myVariants(): List<LookupElement> = emptyList()

    override fun subtype(identifier: String): TypeDescriptor
            = TypeDescriptor.empty()

    override fun name(): String = name
    override fun isArray(): Boolean = false
    override fun isIterable(): Boolean = false
    override fun isMap(): Boolean = false
}