package com.aemtools.lang.clientlib.psi.pattern

import com.aemtools.test.pattern.BasePatternsTest
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Primshyts
 */
class CdPatternsTest : BasePatternsTest() {

  fun testBasePathMain() = testCdPattern(
      CdPatterns.basePath,
      "#base=$CARET",
      true
  )

  fun testBasePath2() = testCdPattern(
      CdPatterns.basePath,
      "#$CARET",
      false
  )

  private fun testCdPattern(pattern: ElementPattern<PsiElement>,
                            text: String,
                            result: Boolean,
                            addCompletionPlaceholder: Boolean = true) =
      testPattern(
          pattern,
          text,
          result,
          addCompletionPlaceholder,
          { textToAdd -> addFile("js.txt", textToAdd) }
      )

}
