package com.aemtools.lang.htl.psi.pattern

import com.aemtools.completion.util.getHtlFile
import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext

/**
 * Will match file that contain injected HTL.
 *
 * @author Dmytro Troynikov
 */
object HtlFilePattern : PatternCondition<PsiFile?>("HTL File") {

  override fun accepts(t: PsiFile, context: ProcessingContext?): Boolean {
    return t.getHtlFile() != null
  }

}
