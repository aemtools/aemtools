package com.aemtools.lang.htl.manipulator

import com.aemtools.lang.htl.psi.HtlElementFactory
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

/**
 * Handles the rename action for [HtlStringLiteral] object which holds the name of Java/Kotlin class.
 *
 * @author Dmytro_Troynikov
 */
class HtlStringLiteralManipulator : AbstractElementManipulator<HtlStringLiteral>() {
  override fun handleContentChange(
      element: HtlStringLiteral,
      range: TextRange,
      newContent: String): HtlStringLiteral {
    val newElement = HtlElementFactory.createStringLiteral(
        newContent,
        element.project,
        element.doubleQuotedString != null)
        ?: return element
    element.node.replaceChild(element.node.firstChildNode, newElement.node.firstChildNode)

    return element
  }
}
