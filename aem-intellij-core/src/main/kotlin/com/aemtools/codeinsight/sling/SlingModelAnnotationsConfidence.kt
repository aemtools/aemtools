package com.aemtools.codeinsight.sling

import com.intellij.codeInsight.completion.CompletionConfidence
import com.intellij.codeInsight.completion.JavaCompletionContributor.ANNOTATION_NAME
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.ThreeState

/**
 * @author Dmytro Troynikov
 */
class SlingModelAnnotationsConfidence : CompletionConfidence() {
  override fun shouldSkipAutopopup(contextElement: PsiElement, psiFile: PsiFile, offset: Int): ThreeState {
    if (ANNOTATION_NAME.accepts(contextElement)) {
      // todo: check that current class is annotated with @Model
      return ThreeState.YES
    }

    return ThreeState.NO
  }
}
