package com.aemtools.blocks.completion.model

import com.aemtools.blocks.base.model.fixture.ITestFixture

/**
 * @author Dmytro Troynikov
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

    fun shouldContain(variants: List<String>, strict: Boolean = true)
}