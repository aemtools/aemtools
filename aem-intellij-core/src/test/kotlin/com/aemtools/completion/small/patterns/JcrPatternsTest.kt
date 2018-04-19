package com.aemtools.completion.small.patterns

import com.aemtools.common.constant.const
import com.aemtools.completion.small.patterns.JcrPatterns.attributeInClientLibraryFolder
import com.aemtools.completion.small.patterns.JcrPatterns.jcrArrayValue
import com.aemtools.completion.small.patterns.JcrPatterns.jcrArrayValueOfCategories
import com.aemtools.test.pattern.BasePatternsTest
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import org.intellij.lang.annotations.Language

/**
 * @author Dmytro Primshyts
 */
class JcrPatternsTest : BasePatternsTest() {

  fun testAttributeInClientLibraryFolder() = xmlPattern(
      attributeInClientLibraryFolder,
      """
        <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            $CARET
      """,
      true
  )

  fun testAttributeInClientLibraryFolderWrongFileName() = xmlPattern(
      attributeInClientLibraryFolder,
      """
        <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            $CARET
      """,
      false,
      fileName = "wrong.xml"
  )

  fun testJcrArrayValue() = xmlPattern(
      jcrArrayValue,
      """
          <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            embed="[$CARET]"
      """,
      true
  )

  fun testJcrArrayValueSecond() = xmlPattern(
      jcrArrayValue,
      """
          <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            embed="[first, $CARET]"
      """,
      true
  )

  fun testJcrArrayValueOfCategories() = xmlPattern(
      jcrArrayValueOfCategories,
      """
          <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            categories="[first, $CARET]" />
      """,
      true
  )

  fun testJcrArrayValueOfCategoriesNegative() = xmlPattern(
      jcrArrayValueOfCategories,
      """
          <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            embed="[first, $CARET]"
      """,
      false
  )

  private fun xmlPattern(
      pattern: ElementPattern<PsiElement>,
      @Language("XML") text: String,
      result: Boolean,
      fileName: String = ".content.xml",
      addCompletionPlaceholder: Boolean = true) = testPattern(
      pattern,
      text,
      result,
      addCompletionPlaceholder,
      { textToAdd -> addXml(fileName, textToAdd) }
  )


}
