package com.aemtools.blocks.base.model.assertion

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * @author Dmytro Troynikov
 */
interface IAssertionContext {

  fun elementUnderCaret(): PsiElement

  fun openedFile(): PsiFile

}
