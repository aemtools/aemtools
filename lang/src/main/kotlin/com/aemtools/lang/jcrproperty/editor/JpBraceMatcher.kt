package com.aemtools.lang.jcrproperty.editor

import com.aemtools.lang.jcrproperty.psi.JpTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

/**
 * @author Dmytro Primshyts
 */
class JpBraceMatcher : PairedBraceMatcher {
  override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

  override fun getPairs(): Array<BracePair> = arrayOf(
      BracePair(JpTypes.LBRACE, JpTypes.RBRACE, false),
      BracePair(JpTypes.LBRACKET, JpTypes.RBRACKET, false)
  )

  override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

}
