package com.aemtools.lang.htl.psi.mixin

import com.aemtools.lang.util.isOption
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiNamedElement
import javax.swing.Icon

/**
 * @author Dmytro_Troynikov
 */
abstract class VariableNameMixin(node: ASTNode)
  : com.aemtools.lang.htl.psi.mixin.HtlELNavigableMixin(node),
    PsiNamedElement, PsiNameIdentifierOwner {

  override fun getName(): String? {
    return text
  }

  override fun setName(name: String): PsiElement {
    val newElement = com.aemtools.lang.htl.psi.HtlElementFactory.createOption(name, project)

    newElement?.let {
      node.replaceChild(node.firstChildNode, it.node.firstChildNode)
    }

    return this
  }

  override fun getNameIdentifier(): PsiElement? {
    return this
  }

  /**
   * Get variable name.
   *
   * @return variable name
   */
  open fun variableName(): String = text

  override fun getPresentation(): ItemPresentation? {
    return object : ItemPresentation {
      override fun getLocationString(): String? {
        return "My location"
      }

      override fun getIcon(unused: Boolean): Icon? = null

      override fun getPresentableText(): String? =
          if (this@VariableNameMixin.isOption()) {
            "context option"
          } else {
            "htl variable"
          }
    }
  }

  override fun isEquivalentTo(another: PsiElement?): Boolean {
    val other = another as? VariableNameMixin
        ?: return false

    return variableName() == other.variableName()
  }

  override fun equals(other: Any?): Boolean {
    return (other as? VariableNameMixin)?.variableName() == variableName()
  }

  override fun hashCode(): Int {
    return variableName().hashCode()
  }

}
