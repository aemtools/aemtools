package com.aemtools.lang.el.editor

import com.aemtools.lang.el.psi.ElTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Primshyts
 */
class ElBraceMatcher : PairedBraceMatcher {
  override fun getCodeConstructStart(
      file: PsiFile?,
      openingBraceOffset: Int): Int = openingBraceOffset

  override fun getPairs(): Array<BracePair> = arrayOf(
      BracePair(ElTypes.LPAREN, ElTypes.RPAREN, false),
      BracePair(ElTypes.LBRACK, ElTypes.RBRACK, false),
      BracePair(ElTypes.START_DYNAMIC_EXPRESSION, ElTypes.END_EXPRESSION, false),
      BracePair(ElTypes.START_DEFERRED_EXPRESSION, ElTypes.END_EXPRESSION, false)
  )

  override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true
}
