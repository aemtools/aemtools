package com.aemtools.common.util

import com.intellij.lang.annotation.Annotation
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * Create info annotation builder in current annotation holder using given text range.
 *
 * @param range text range to which the highlight should be applied
 * @param textAttributesKey highlight style
 * @param message text message (`null` by default)
 *
 * @receiver [AnnotationHolder]
 * @return [AnnotationBuilder] object]
 */
fun AnnotationHolder.createInfoAnnotationBuilder(range: TextRange,
                                                 textAttributesKey: TextAttributesKey,
                                                 message: String? = null): AnnotationBuilder {
  val annotationBuilder = if (message == null) {
    newSilentAnnotation(HighlightSeverity.WEAK_WARNING)
  } else {
    newAnnotation(HighlightSeverity.WEAK_WARNING, message)
  }
  return annotationBuilder
      .range(range)
      .textAttributes(textAttributesKey)
}

/**
 * Create info annotation in current annotation holder using given text range.
 *
 * @param range text range to which the highlight should be applied
 * @param textAttributesKey highlight style
 * @param message text message (`null` by default)
 *
 * @receiver [AnnotationHolder]
 */
fun AnnotationHolder.createInfoAnnotation(range: TextRange,
                                          textAttributesKey: TextAttributesKey,
                                          message: String? = null): Unit =
    createInfoAnnotationBuilder(range, textAttributesKey, message).create()

/**
 * Create info annotation builder in current annotation holder using given psi element.
 *
 * @param element the element to highlight
 * @param textAttributesKey highlight style
 * @param message text message (`null` by default)
 *
 * @receiver [AnnotationHolder]
 * @return [AnnotationBuilder] object
 */
fun AnnotationHolder.createInfoAnnotationBuilder(element: PsiElement,
                                                 textAttributesKey: TextAttributesKey,
                                                 message: String? = null): AnnotationBuilder =
    createInfoAnnotationBuilder(element.textRange, textAttributesKey, message)

/**
 * Create info annotation in current annotation holder using given psi element.
 *
 * @param element the element to highlight
 * @param textAttributesKey highlight style
 * @param message text message (`null` by default)
 *
 * @receiver [AnnotationHolder]
 */
fun AnnotationHolder.createInfoAnnotation(element: PsiElement,
                                          textAttributesKey: TextAttributesKey,
                                          message: String? = null): Unit =
    createInfoAnnotationBuilder(element, textAttributesKey, message).create()

/**
 * Create error annotation in current annotation holder using given psi element.
 *
 * @param element the element that should be marked with error
 * @param message error message (`null` by default)
 *
 * @receiver [AnnotationHolder]
 */
fun AnnotationHolder.error(element: PsiElement,
                           message: String?) {
  val annotationBuilder = if (message == null) {
    newSilentAnnotation(HighlightSeverity.WEAK_WARNING)
  } else {
    newAnnotation(HighlightSeverity.WEAK_WARNING, message)
  }
  return annotationBuilder
      .range(element.textRange)
      .create()
}

