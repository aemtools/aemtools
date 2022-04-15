package com.aemtools.lang.el.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

/**
 * @author Dmytro Primshyts
 */
open class ElPsiBaseElement(node: ASTNode)
  : ASTWrapperPsiElement(node)
