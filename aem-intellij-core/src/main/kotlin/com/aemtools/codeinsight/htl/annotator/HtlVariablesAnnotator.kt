package com.aemtools.codeinsight.htl.annotator

import com.aemtools.common.util.closest
import com.aemtools.common.util.distanceTo
import com.aemtools.common.util.hasParentOfType
import com.aemtools.common.util.highlight
import com.aemtools.common.util.toSmartPointer
import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.common.PredefinedVariables
import com.aemtools.completion.model.htl.ContextObject
import com.aemtools.inspection.fix.VariableNameErrataIntentionAction
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_GLOBAL_VARIABLE
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_LOCAL_VARIABLE
import com.aemtools.lang.htl.colorscheme.HtlColors.HTL_EL_UNRESOLVED_VARIABLE
import com.aemtools.lang.htl.psi.HtlAccessIdentifier
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.util.isOption
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

/**
 * Annotates htl variables inside of EL.
 *
 * @author Dmytro Primshyts
 */
class HtlVariablesAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element !is VariableNameMixin
        || element.isOption()
        || element.hasParentOfType(HtlAccessIdentifier::class.java)) {
      return
    }

    val name = element.variableName()
    val contextObjects = PredefinedVariables.allContextObjects()
    when {
      contextObjects.find { it.name == name } != null -> {
        holder.highlight(element, HTL_EL_GLOBAL_VARIABLE, "Context Object")
      }

      FileVariablesResolver.validVariable(name, element) -> {
        holder.highlight(element, HTL_EL_LOCAL_VARIABLE)
      }

      else -> {
        val annotation = holder.highlight(element,
            HTL_EL_UNRESOLVED_VARIABLE,
            "Cannot resolve symbol '$name'")

        val similar = findSimilarVariable(element, contextObjects, name)
        if (similar != null) {
          annotation.registerFix(
              VariableNameErrataIntentionAction(
                  similar,
                  element.toSmartPointer()
              )
          )
        }
      }
    }
  }

  private fun findSimilarVariable(element: PsiElement,
                                  contextObjects: List<ContextObject>,
                                  name: String): String? {
    val variables = FileVariablesResolver.declarationsForPosition(element)

    val availableNames = (variables.map { it.variableName }
        + contextObjects.map { it.name })

    val closest = name.closest(availableNames.toSet())
        ?: return null

    return if (closest.distanceTo(name) < name.length / 2) {
      closest
    } else {
      null
    }
  }

}
