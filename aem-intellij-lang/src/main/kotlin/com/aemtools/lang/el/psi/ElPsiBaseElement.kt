package com.aemtools.lang.el.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

/**
 * @author Dmytro Troynikov
 */
open class ElPsiBaseElement(node: ASTNode)
  : ASTWrapperPsiElement(node)
