package com.aemtools.lang.htl.psi.mixin

import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.hasChild
import com.aemtools.lang.htl.psi.HtlArrayLikeAccess
import com.aemtools.lang.htl.psi.HtlElementFactory
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Primshyts
 */
abstract class AccessIdentifierMixin(node: ASTNode) : com.aemtools.lang.htl.psi.mixin.VariableNameMixin(node) {

  override fun getName(): String? =
      variableName()

  override fun setName(name: String): PsiElement {
    val newElement = when {
      this.hasChild(HtlArrayLikeAccess::class.java) -> {
        val stringLiteral = this.findChildrenByType(HtlStringLiteral::class.java)
            .firstOrNull() ?: return this

        if (stringLiteral.text.startsWith("\"")) {
          HtlElementFactory.createArrayLikeAccessDoublequoted(name, project)
        } else {
          HtlElementFactory.createArrayLikeAccessSingleQuoted(name, project)
        }
      }

      else -> HtlElementFactory.createDotAccessIdentifier(name, project)
    }
    newElement?.let {
      node.replaceChild(node.firstChildNode, it)
    }

    return this
  }

  /**
   * Extracts the name of the variable
   * e.g.
   *
   * ```
   * .name -> name
   * ["name"] -> name
   * ['name'] -> name
   * ```
   * @return the variable name
   */
  override fun variableName(): String {
    return when {
      text.startsWith(".") ->
        text.substring(1)
      text.startsWith("['") || text.startsWith("[\"") ->
        text.substring(2, text.length - 2)
      else -> ""
    }
  }

}
