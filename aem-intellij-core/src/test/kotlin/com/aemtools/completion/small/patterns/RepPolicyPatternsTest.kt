package com.aemtools.completion.small.patterns

import com.aemtools.completion.small.patterns.RepPolicyPatterns.primaryTypeInAcl
import com.aemtools.test.pattern.BasePatternsTest
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import org.intellij.lang.annotations.Language

/**
 * Test for [RepPolicyPatterns].
 *
 * @author Dmytro Primshyts
 */
class RepPolicyPatternsTest : BasePatternsTest() {

  fun testPrimaryTypeInAcl() = xmlPattern(
      primaryTypeInAcl,
      """
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:rep="internal"
      jcr:primaryType="rep:ACL">
    <unknownName jcr:primaryType="$CARET"
</jcr:root>
      """,
       true
  )

  private fun xmlPattern(
      pattern: ElementPattern<PsiElement>,
      @Language("XML") text: String,
      result: Boolean,
      fileName: String = "_rep_policy.xml",
      addCompletionPlaceholder: Boolean = true) = testPattern(
      pattern,
      text,
      result,
      addCompletionPlaceholder,
      { textToAdd -> addXml(fileName, textToAdd) }
  )
}
