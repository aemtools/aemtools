package com.aemtools.codeinsight.xml

import com.aemtools.common.constant.Const.FileNames.CQ_DIALOG_XML
import com.aemtools.common.constant.Const.FileNames.CQ_EDITCONFIG_XML
import com.aemtools.common.constant.Const.FileNames.DIALOG_XML
import com.aemtools.common.constant.Const.FileNames.FILE_VAULT_FILE_NAME
import com.aemtools.common.constant.Const.JCR_PRIMARY_TYPE
import com.intellij.codeInsight.daemon.impl.analysis.InsertRequiredAttributeFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag

class JcrPrimaryTypeAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val xmlTag = element as? XmlTag ?: return
    if (!PRIMARY_TYPE_ANNOTATOR_XML_FILE_PATTER.accepts(element.containingFile)) {
      return
    }
    val attribute = xmlTag.getAttribute(JCR_PRIMARY_TYPE)
    if (attribute != null) {
      return
    }
    val message = "Node must have '$JCR_PRIMARY_TYPE' attribute."
    holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message)
      .range(xmlTag)
      .highlightType(ProblemHighlightType.POSSIBLE_PROBLEM)
      .withFix(InsertRequiredAttributeFix(xmlTag, JCR_PRIMARY_TYPE, "nt:unstructured"))
      .create()
  }

  companion object {
    val PRIMARY_TYPE_ANNOTATOR_XML_FILE_PATTER = XmlPatterns
      .xmlFile()
      .withName(FILE_VAULT_FILE_NAME, CQ_DIALOG_XML, CQ_EDITCONFIG_XML, DIALOG_XML)
  }
}
