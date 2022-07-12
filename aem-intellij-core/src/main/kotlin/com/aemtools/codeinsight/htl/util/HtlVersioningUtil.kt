package com.aemtools.codeinsight.htl.util

import com.aemtools.codeinsight.htl.intention.ChangeHtlVersionAction
import com.aemtools.lang.settings.AemProjectSettings
import com.aemtools.lang.settings.model.HtlVersion
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * Create error annotation builder to annotate HTL unsupported feature.
 *
 * @param element the element that should be marked with error
 * @param textRange text range to annotate
 *
 * @receiver [AnnotationHolder]
 * @return [AnnotationBuilder]
 */
fun AnnotationHolder.notSupportedHtlFeatureAnnotationBuilder(
    element: PsiElement,
    textRange: TextRange = element.textRange): AnnotationBuilder {
  val currentProjectHtlVersion = element.project.getHtlVersion()
  val message = "Support for this option starts with HTL version $currentProjectHtlVersion."
  return this.notSupportedHtlFeatureAnnotationBuilder(element, message, textRange)
}

/**
 * Create error annotation builder to annotate HTL unsupported feature.
 *
 * @param element the element that should be marked with error
 * @param message the message that should be shown in tooltip
 * @param textRange text range to annotate
 *
 * @receiver [AnnotationHolder]
 * @return [AnnotationBuilder]
 */
fun AnnotationHolder.notSupportedHtlFeatureAnnotationBuilder(
    element: PsiElement,
    message: String,
    textRange: TextRange = element.textRange): AnnotationBuilder {
  return this.newAnnotation(HighlightSeverity.ERROR, message)
      .textAttributes(CodeInsightColors.ERRORS_ATTRIBUTES)
      .withFix(ChangeHtlVersionAction())
      .range(textRange)
}

/**
 * Returns current project HTL version.
 *
 * @receiver [Project]
 */
fun Project.getHtlVersion(): HtlVersion = AemProjectSettings.getInstance(this).htlVersion


/**
 * Prepares template not supported HTL feature text.
 *
 * @param projectHtlVersion the current project HTL version
 * @param sinceHtlVersion the HTL version since feature is available
 */
fun notSupportedHtlFeatureText(projectHtlVersion: HtlVersion, sinceHtlVersion: HtlVersion) =
    """
      Support for this option starts with HTL version ${sinceHtlVersion.version}. 
      Current project version - ${projectHtlVersion.version}.
    """.trimIndent()
