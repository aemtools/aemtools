package com.aemtools.lang.htl.editor

import com.aemtools.lang.htl.psi.HtlTypes.EL_START
import com.aemtools.lang.htl.psi.HtlTypes.LBRACKET
import com.aemtools.lang.htl.psi.HtlTypes.LSQRBRACKET
import com.aemtools.lang.htl.psi.HtlTypes.RBRACE
import com.aemtools.lang.htl.psi.HtlTypes.RBRACKET
import com.aemtools.lang.htl.psi.HtlTypes.RSQRBRACKET
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro_Troynikov
 */
class HtlBraceMatcher : PairedBraceMatcher {
  override fun getCodeConstructStart(
      file: PsiFile?,
      openingBraceOffset: Int): Int = openingBraceOffset

  override fun getPairs(): Array<out BracePair> = arrayOf(
      BracePair(LSQRBRACKET, RSQRBRACKET, false),
      BracePair(LBRACKET, RBRACKET, false),
      BracePair(EL_START, RBRACE, false)
  )

  override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

}
