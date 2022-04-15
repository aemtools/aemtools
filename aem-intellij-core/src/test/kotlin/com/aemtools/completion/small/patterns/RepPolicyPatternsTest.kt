package com.aemtools.completion.small.patterns

import com.aemtools.completion.small.patterns.RepPolicyPatterns.attributeNameUnderAcl
import com.aemtools.completion.small.patterns.RepPolicyPatterns.primaryTypeInAcl
import com.aemtools.completion.small.patterns.RepPolicyPatterns.privilegesValue
import com.aemtools.completion.small.patterns.RepPolicyPatterns.repRestrictionAttributeName
import com.aemtools.completion.small.patterns.RepPolicyPatterns.tagUnderAclRoot
import com.aemtools.completion.small.patterns.RepPolicyPatterns.tagUnderAllowOrDeny
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

  fun testAttributeNameUnderAcl() = xmlPattern(
      attributeNameUnderAcl,
      """
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:rep="internal"
      jcr:primaryType="rep:ACL">
    <unknownName $CARET
</jcr:root>
      """,
      true
  )

  fun testPrivilegesValue() = xmlPattern(
      privilegesValue,
      """
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:rep="internal"
      jcr:primaryType="rep:ACL">
    <unknownName rep:privileges="{Name}[$CARET]"
</jcr:root>
      """,
      true
  )

  fun testPrivilegesValueSecondPosition() = xmlPattern(
      privilegesValue,
      """
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:rep="internal"
      jcr:primaryType="rep:ACL">
    <unknownName rep:privileges="{Name}[write, $CARET]"
</jcr:root>
      """,
      true
  )

  fun testRepRestrictionAttributeName() = xmlPattern(
      repRestrictionAttributeName,
      """
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:rep="internal"
      jcr:primaryType="rep:ACL">
    <allow>
        <rep:restrictions $CARET
    </allow>
</jcr:root>
      """,
      true
  )

  fun testTagUnderAclRoot() = xmlPattern(
      tagUnderAclRoot,
      """
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:rep="internal"
      jcr:primaryType="rep:ACL">
    <a$CARET
</jcr:root>
      """,
      true
  )

  fun testTagUnderAllowOrDeny() = xmlPattern(
      tagUnderAllowOrDeny,
      """
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:rep="internal"
      jcr:primaryType="rep:ACL">
    <allow>
        <re$CARET
    </allow>
</jcr:root>
      """,
      true
  )

  private fun xmlPattern(
      pattern: ElementPattern<out PsiElement>,
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
