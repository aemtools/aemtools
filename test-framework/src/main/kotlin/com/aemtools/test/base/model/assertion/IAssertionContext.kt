package com.aemtools.test.base.model.assertion

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * @author Dmytro Primshyts
 */
interface IAssertionContext {

  fun elementUnderCaret(): PsiElement

  fun openedFile(): PsiFile

}
