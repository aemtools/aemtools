package com.aemtools.completion.small.patterns

import com.aemtools.common.constant.const
import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfCategories
import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfDependencies
import com.aemtools.completion.small.patterns.ClientlibraryFolderPatterns.jcrArrayValueOfEmbeds
import com.aemtools.test.pattern.BasePatternsTest
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import org.intellij.lang.annotations.Language

/**
 * Test for [ClientlibraryFolderPatterns].
 *
 * @author Dmytro Primshyts
 */
class ClientlibraryFolderPatternsTest : BasePatternsTest() {

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

  fun testJcrArrayValueOfDependencies() = xmlPattern(
      jcrArrayValueOfDependencies,
      """
          <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            dependencies="[first, $CARET]" />
      """,
      true
  )

  fun testJcrArrayValueOfDependenciesNegative() = xmlPattern(
      jcrArrayValueOfDependencies,
      """
          <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            categories="[first, $CARET]" />
      """,
      false
  )

  fun testJcrArrayValueOfEmbeds() = xmlPattern(
      jcrArrayValueOfEmbeds,
      """
          <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            embed="[first, $CARET]" />
      """,
      true
  )

  fun testJcrArrayValueOfEmbedsNegative() = xmlPattern(
      jcrArrayValueOfEmbeds,
      """
          <jcr:root jcr:primaryType="${const.xml.CQ_CLIENTLIBRARY_FOLDER}"
            dependencies="[first, $CARET]" />
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
