package com.aemtools.findusages

import com.aemtools.lang.htl.lexer.HtlLexer
import com.aemtools.lang.htl.psi.HtlTypes
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class HtlVariableUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner? = DefaultWordsScanner(
            HtlLexer(),
            TokenSet.create(HtlTypes.VAR_NAME,
                    HtlTypes.VARIABLE_NAME,
                    HtlTypes.ACCESS_IDENTIFIER,
                    HtlTypes.DOUBLE_QUOTED_STRING,
                    HtlTypes.SINGLE_QUOTED_STRING),
            TokenSet.EMPTY,
            TokenSet.create(HtlTypes.INTEGER, HtlTypes.TRUE, HtlTypes.FALSE)
    )

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return if (element is XmlAttribute) {
            element.name.substringAfter(".", "item")
        } else {
            element.text
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return "Description: ${element.text}"
    }

    override fun getType(element: PsiElement): String {
        return "HTL Variable"
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return false
//        return psiElement is XmlAttribute && psiElement.isHtlDeclarationAttribute()
    }

}