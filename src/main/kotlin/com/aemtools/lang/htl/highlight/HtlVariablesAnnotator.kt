package com.aemtools.lang.htl.highlight

import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.common.PredefinedVariables
import com.aemtools.completion.util.hasParent
import com.aemtools.completion.util.isOption
import com.aemtools.lang.common.HtlHighlightColors.HTL_EL_GLOBAL_VARIABLE
import com.aemtools.lang.common.HtlHighlightColors.HTL_EL_LOCAL_VARIABLE
import com.aemtools.lang.common.HtlHighlightColors.HTL_EL_UNRESOLVED_VARIABLE
import com.aemtools.lang.common.highlight
import com.aemtools.lang.htl.psi.HtlAccessIdentifier
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

/**
 * Annotates htl variables inside of EL.
 *
 * @author Dmytro Troynikov
 */
class HtlVariablesAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is VariableNameMixin
                || element.isOption()
                || element.hasParent(HtlAccessIdentifier::class.java)) {
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
                holder.highlight(element, HTL_EL_UNRESOLVED_VARIABLE, "Cannot resolve symbol '$name'")
            }
        }

    }

}