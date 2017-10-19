package com.aemtools.analysis.htl.callchain.typedescriptor.properties

import com.aemtools.analysis.htl.callchain.typedescriptor.base.BaseTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
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
class PropertiesTypeDescriptor(val element: PsiElement) : BaseTypeDescriptor() {

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

}
