package com.aemtools.completion.small.patterns

import com.aemtools.completion.small.patterns.EditConfigPatterns.attributeUnderJcrRoot
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqActionsValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqDialogModeValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqInheritValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.cqLayoutValue
import com.aemtools.completion.small.patterns.EditConfigPatterns.primaryTypeInEditConfig
import com.aemtools.test.pattern.BasePatternsTest
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import org.intellij.lang.annotations.Language

/**
 * @author Dmytro Primshyts
 */
class EditConfigPatternsTest : BasePatternsTest() {

  fun testPrimaryTypeInEditConfig() = xmlPattern(
      primaryTypeInEditConfig,
      """
          <jcr:root jcr:primaryType="$CARET"></jcr:root>
      """,
      true
  )

  fun testCqActionsValue() = xmlPattern(
      cqActionsValue,
      """
          <jcr:root cq:actions="[$CARET]"></jcr:root>
      """,
      true
  )

  fun testCqActionsValueSecondPosition() = xmlPattern(
      cqActionsValue,
      """
          <jcr:root cq:actions="[edit,$CARET]"></jcr:root>
      """,
      true
  )

  fun testAttributeUnderJcrRoot() = xmlPattern(
      attributeUnderJcrRoot,
      """
          <jcr:root $CARET ></jcr:root>
      """,
      true
  )

  fun testCqDialogModeValue() = xmlPattern(
      cqDialogModeValue,
      """
          <jcr:root cq:dialogMode="$CARET"></jcr:root>
      """,
      true
  )

  fun testCqInheritValue() = xmlPattern(
      cqInheritValue,
      """
          <jcr:root cq:inherit="$CARET"></jcr:root>
      """,
      true
  )

  fun testCqLayoutValue() = xmlPattern(
      cqLayoutValue,
      """
          <jcr:root cq:layout="$CARET"></jcr:root>
      """,
      true
  )

  private fun xmlPattern(
      pattern: ElementPattern<out PsiElement>,
      @Language("XML") text: String,
      result: Boolean,
      fileName: String = "_cq_editConfig.xml",
      addCompletionPlaceholder: Boolean = true) = testPattern(
      pattern,
      text,
      result,
      addCompletionPlaceholder,
      { textToAdd -> addXml(fileName, textToAdd) }
  )
}
