package com.aemtools.index.model.dialog.parameter

import com.aemtools.common.completion.lookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.icons.AllIcons
import java.io.Serializable

/**
 * Base dialog parameter declaration class.
 *
 * @property name The name of parameter.
 *
 * @author Dmytro Troynikov
 */
abstract class BaseParameterDeclaration : Serializable {

  abstract val name: String

  protected abstract val tailText: String

  /**
   * Convert current parameter declaration into [LookupElement].
   *
   * @return lookup element
   */
  fun toLookupElement(): LookupElement =
      lookupElement(name.normalize())
          .withIcon(AllIcons.Nodes.Parameter)
          .withTailText("($tailText)", true)
          .withTypeText("Dialog")

  /**
   * Normalize dialog parameter name.
   * In case if parameter has "./" prefix it will be removed.
   *
   * @receiver [String]
   * @return normalized parameter name
   */
  private fun String.normalize() = if (this.startsWith("./")) {
    this.substringAfter("./")
  } else {
    this
  }

}
