package com.aemtools.analysis.htl.callchain.typedescriptor.template

import com.aemtools.analysis.htl.callchain.typedescriptor.base.BaseTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.lang.htl.icons.HtlIcons
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project

/**
 * Descriptor of type spawned in `data-sly-use` which included some Htl template containing file.
 *
 * @author Dmytro Troynikov
 */
class TemplateHolderTypeDescriptor(
    val templates: List<TemplateDefinition>,
    val project: Project)
  : BaseTypeDescriptor() {
  override fun myVariants(): List<LookupElement> {
    return templates.map {
      LookupElementBuilder.create(it.name)
          .withTypeText("HTL Template")
          .withIcon(HtlIcons.HTL_FILE_ICON)
    }
  }

  override fun subtype(identifier: String): TypeDescriptor {
    return templates.find { it.name == identifier }
        .toTypeDescriptor()
  }

  override fun asResolutionResult(): ResolutionResult =
      ResolutionResult(null, myVariants())

  private fun TemplateDefinition?.toTypeDescriptor(): TypeDescriptor =
      if (this != null) {
        TemplateTypeDescriptor(this, project)
      } else {
        TypeDescriptor.empty()
      }

}

