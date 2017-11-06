package com.aemtools.lang.html.inspection

import com.aemtools.completion.util.htlAttributeName
import com.aemtools.completion.util.toSmartPointer
import com.aemtools.constant.const.htl.DATA_SLY_ATTRIBUTE
import com.aemtools.constant.const.html.JS_ATTRIBUTES
import com.aemtools.inspection.fix.BaseHtlFix
import com.intellij.codeInsight.daemon.impl.analysis.RemoveAttributeIntentionFix
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class MessedSlyAttributeAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val attribute = element as? XmlAttribute ?: return
    val htlAttributeName = attribute.htlAttributeName()
    if (htlAttributeName != DATA_SLY_ATTRIBUTE) {
      return
    }

    val htlVariableName = attribute.name.substringAfter(".")

    if (htlVariableName == "style" || htlVariableName in JS_ATTRIBUTES) {
      holder.createWarningAnnotation(
          element,
          "data-sly-attribute with $htlVariableName will be ignored"
      ).let {
        it.registerFix(RemoveAttributeIntentionFix(attribute.name, attribute))
        it.registerFix(SubstituteWithRawAttributeFix(
            attribute.toSmartPointer(),
            "Replace with: $htlVariableName=\"${attribute.value}\""
        )
        )
      }
    }
  }

  class SubstituteWithRawAttributeFix(
      private val pointer: SmartPsiElementPointer<XmlAttribute>,
      private val message: String
  ) : BaseHtlFix({ message }), LocalQuickFix {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
      invoke(project, null, null)
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
      val attribute = pointer.element ?: return

      attribute.name = attribute.name.substringAfterLast(".")
    }

  }

}

