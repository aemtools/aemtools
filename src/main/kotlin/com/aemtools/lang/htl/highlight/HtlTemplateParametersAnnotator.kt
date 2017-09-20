package com.aemtools.lang.htl.highlight

import com.aemtools.completion.util.isInsideOf
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.aemtools.completion.util.isOption
import com.aemtools.lang.common.highlight
import com.aemtools.lang.htl.colorscheme.HtlColors

/**
 * @author Dmytro Troynikov
 */
class HtlTemplateParametersAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is VariableNameMixin
                || !element.isOption()
                || !element.isInsideOf("data-sly-call")) {
            return
        }

        holder.highlight(element, HtlColors.OPTION)
    }

}
