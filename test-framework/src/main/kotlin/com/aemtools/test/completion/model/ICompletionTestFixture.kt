package com.aemtools.test.completion.model

import com.aemtools.test.base.model.fixture.ITestFixture

/**
 * @author Dmytro Primshyts
 */
interface ICompletionTestFixture : ITestFixture {

  /**
   * Sets the completion type to [com.intellij.codeInsight.completion.CompletionType.BASIC]
   * (used by default)
   */
  fun basic()

  /**
   * Sets the completion type to [com.intellij.codeInsight.completion.CompletionType.SMART]
   */
  fun smart()

  /**
   * List the completion variants that should be presented within current fixture.
   *
   * @param variants the list of variants
   * @param strict if *true* the completion should contain *only* listed variants if *false*
   * it may contain some additional variants. true is default.
   * @param ordered *true* means that variants should be checked to go in given order
   */
  fun shouldContain(variants: List<String>,
                    strict: Boolean = true,
                    ordered: Boolean = false)

  /**
   * Vararg version of [shouldContain].
   */
  fun shouldContain(vararg variants: String) = shouldContain(listOf(*variants), true)

  fun shouldNotContain(variants: List<String>)

  fun shouldNotContain(vararg variants: String) = shouldNotContain(listOf(*variants))
}
