package com.aemtools.lang.el

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.xml.XmlAttributeValue

/**
 * @author Dmytro Troynikov
 */
class ElInjector : MultiHostInjector {
  override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>>
      = listOf(XmlAttributeValue::class.java).toMutableList()

  override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
    val xmlAttributeValue = context as? XmlAttributeValue ?: return

    if (context.containingFile.name != ".content.xml") {
      return
    }

    val text = xmlAttributeValue.value ?: return

    val elOccurences = extractEl(text)

    if (elOccurences.isNotEmpty()) {
      registrar.startInjecting(ElLanguage)

      elOccurences.forEach {textRange->
        if (context is PsiLanguageInjectionHost) {
          registrar.addPlace(
              null,
              null,
              context,
              textRange.shiftRight(1))
        }
      }

      registrar.doneInjecting()
    }
  }

  private fun extractEl(text: String) : List<TextRange> {
    val result = ArrayList<TextRange>()
    var startIndex = 0
    while (true) {
      var start = text.indexOf("\${", startIndex)

      if (start == -1) {
        break
      }

      var close = findCloseBrace(start, text)
      if (close == -1) {
        break
      }

      result += TextRange(start, close)
      startIndex = close
    }

    return result
  }

  private fun findCloseBrace(start: Int, text: String): Int {
    var openBraces = 0

    var inString = false

    var quote: Char? = null
    for (position in start..text.length - 1) {
      val char = text[position]

      // braces within string literals should be ignored
      if (char in listOf('"', '\'')) {
        if (position == 0 || text[position - 1] != '\\') {
          inString = !inString
          continue
        }
      }

      if (inString) {
        continue
      }

      when {
        char == '{' -> {
          openBraces++
        }
        char == '}' && openBraces != 0 -> {
          openBraces--
        }
      }

      if (char == '}' && openBraces == 0) {
        return position + 1
      }
    }

    return -1
  }

}
