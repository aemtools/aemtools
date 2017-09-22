package com.aemtools.lang.htl.manipulator

import com.aemtools.lang.htl.psi.HtlElementFactory
import com.aemtools.lang.htl.psi.HtlVariableName
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

/**
 * @author Dmytro Troynikov
 */
class HtlVariableNameManipulator : AbstractElementManipulator<HtlVariableName>() {
  override fun handleContentChange(element: HtlVariableName, range: TextRange, newContent: String): HtlVariableName {
    val newElement = HtlElementFactory.createOption(newContent, element.project)
        ?: return element

    element.node.replaceChild(
        element.node.firstChildNode,
        newElement.node.firstChildNode
    )
    return element
  }
}
