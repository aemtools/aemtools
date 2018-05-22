package com.aemtools.lang.htl.psi

import com.aemtools.lang.htl.HtlLanguage
import com.intellij.lang.ASTNode
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.ILeafElementType

/**
 * Created by Dmytro_Troynikov on 5/16/2016.
 */
class HtlTokenType(debugName: String) :
    IElementType(debugName, HtlLanguage), ILeafElementType {

  override fun createLeafNode(leafText: CharSequence): ASTNode {
    return LeafPsiElement(this, leafText)
  }

}
