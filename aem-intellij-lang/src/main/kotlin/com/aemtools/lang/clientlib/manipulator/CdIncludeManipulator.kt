package com.aemtools.lang.clientlib.manipulator

import com.aemtools.lang.clientlib.psi.CdElementFactory
import com.aemtools.lang.clientlib.psi.CdInclude
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class CdIncludeManipulator : AbstractElementManipulator<CdInclude>() {
  override fun handleContentChange(
      element: CdInclude,
      range: TextRange,
      newContent: String): CdInclude {
    val oldFileName = element.text.substringAfterLast("/")
    val newFilePath = element.text.replace(oldFileName, newContent)

    val newCdSimpleInclude = CdElementFactory.createCdInclude(
        newFilePath,
        element.project) ?: return element

    element.node.getChildren(null).forEach {
      element.node.removeChild(it)
    }

    newCdSimpleInclude.node.getChildren(null).forEach {
      element.node.addChild(it)
    }

    return element
  }
}
