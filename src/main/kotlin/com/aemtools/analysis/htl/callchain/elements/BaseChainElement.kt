package com.aemtools.analysis.htl.callchain.elements

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
open class BaseChainElement(
    override val element: PsiElement,
    override val name: String,
    override val type: TypeDescriptor
) : CallChainElement
