package com.aemtools.lang.htl.manipulator

import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.JavaPsiFacade

/**
 * Handles the rename action for [HtlStringLiteral] object which holds the name of Java/Kotlin class.
 * @author Dmytro_Troynikov
 */
class HtlStringLiteralManipulator : AbstractElementManipulator<HtlStringLiteral>() {
    override fun handleContentChange(element: HtlStringLiteral, range: TextRange, newContent: String): HtlStringLiteral {
        val oldText = element.text
        val newText = oldText.substring(0, range.startOffset) + newContent + oldText.substring(range.endOffset)
        val newElement = JavaPsiFacade.getInstance(element.project)
                .elementFactory.createExpressionFromText(newText, null)
        return element.replace(newElement) as HtlStringLiteral
    }
}