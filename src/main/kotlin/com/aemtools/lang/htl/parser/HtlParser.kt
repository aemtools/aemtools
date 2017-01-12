package com.aemtools.lang.htl.parser

import com.aemtools.lang.htl.HtlParser
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

/**
 * Created by Dmytro_Troynikov on 12/21/2016.
 */
class HtlParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val parser = HtlParser()
        val result = parser.parse(root, builder)
        return result
    }
}