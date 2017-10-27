package com.aemtools.completion.util

import com.aemtools.constant.const
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro_Troynikov
 */
@Deprecated("Use corresponding extension functions instead")
object PsiXmlUtil {

  /**
   * Extracts the tag containing given element (itself in case if passed element is tag itself)
   * @param element the starting point of lookup
   * @return tag container *null* if no tag found
   */
  @Deprecated("Legacy code", ReplaceWith("PsiElement.findParentByType(Class<T>)"), DeprecationLevel.WARNING)
  fun extractTag(element: PsiElement): XmlTag? {
    var currentElement: PsiElement? = element
    while (currentElement != null) {
      if (currentElement is XmlTag) {
        return currentElement
      }
      currentElement = currentElement.parent
    }
    return null
  }

  /**
   * Removes Idea placeholder which denotes the position of caret.
   *
   *
   * __Example input:__
   *
   *
   *    "pathfIntellijIdeaRulezzzield" -> "pathfield"
   *
   *
   *    "IntellijIdeaRulezzz" -> ""
   * @param input string to process.
   * @return the input string without caret placeholder.
   */
  @Deprecated("Legacy code")
  fun removeCaretPlaceholder(input: String?): String? {
    if (input == null) {
      return null
    }

    if (input.contains(const.IDEA_STRING_CARET_PLACEHOLDER)) {
      return input.substring(0, input.indexOf(const.IDEA_STRING_CARET_PLACEHOLDER))
    }

    return input
  }

  /**
   * Extract name of attribute associated with given PsiElement
   */
  @Deprecated("Legacy code", ReplaceWith("PsiElement?.findParentByType(XmlAttribute::class.java)?.name"))
  fun nameOfAttribute(element: PsiElement?): String? {
    if (element == null) {
      return null
    }

    val attr: PsiElement? = element.findParentByType(XmlAttribute::class.java) ?: return null

    return (attr as? XmlAttribute)?.name
  }

}
