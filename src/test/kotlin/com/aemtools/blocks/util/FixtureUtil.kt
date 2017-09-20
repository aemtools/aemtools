package com.aemtools.blocks.util

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

/**
 * Find quick fix of specified type in given test file.
 *
 * @param FIX type of quick fix
 * @param fileName the name of test file
 * @receiver [CodeInsightTestFixture]
 * @throws [AssertionError] in case if more than one quick fix of given type was found in given file
 * @return instance of quick fix object, `null` if no object found
 */
inline fun <reified FIX : IntentionAction> CodeInsightTestFixture.quickFix(fileName: String): FIX? {
  val fixes: List<FIX> = quickFixes(fileName)
  if (fixes.size > 1) {
    throw AssertionError("Found several quick fixes of type ${FIX::class.java}\nIn file: $fileName")
  }
  return fixes.firstOrNull()
}

/**
 * Collect all quick fixes of given type from current fixture.
 *
 * @param fileName file to collect quick fixes from
 *
 * @receiver [CodeInsightTestFixture]
 * @return list of quick fixes, will be empty if no fixes with given type found
 */
inline fun <reified FIX : IntentionAction> CodeInsightTestFixture.quickFixes(fileName: String): List<FIX> =
    getAllQuickFixes(fileName)
        .mapNotNull { it as? FIX }
