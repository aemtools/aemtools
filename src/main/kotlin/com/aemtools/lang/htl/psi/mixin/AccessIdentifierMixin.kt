package com.aemtools.lang.htl.psi.mixin

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.hasChild
import com.aemtools.lang.htl.psi.HtlArrayLikeAccess
import com.aemtools.lang.htl.psi.HtlElementFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

/**
 * @author Dmytro_Troynikov
 */
abstract class AccessIdentifierMixin(node: ASTNode) : VariableNameMixin(node) {

  override fun getName(): String? =
      variableName()

  override fun setName(name: String): PsiElement {
    val newElement = when {
      this.hasChild(HtlArrayLikeAccess::class.java) -> {
        val stringLiteral = this.findChildrenByType(com.aemtools.lang.htl.psi.HtlStringLiteral::class.java)
            .firstOrNull() ?: return this

        if (stringLiteral.doubleQuotedString != null) {
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
