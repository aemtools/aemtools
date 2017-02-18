package com.aemtools.blocks.completion.model

import com.aemtools.blocks.base.model.fixture.TestFixture
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Dmytro Troynikov
 */
class CompletionTestFixture(fixture: JavaCodeInsightTestFixture)
    : TestFixture(fixture), ICompletionTestFixture {

    var completionType = CompletionType.BASIC
    var shouldContain: List<String> = listOf()
    var shouldContainStrict: Boolean = true

    override fun test() {
        super.test()
        val completionVariants = fixture.complete(completionType).toList()
                .map {it.lookupString}
        if (shouldContainStrict) {
            assertThat(completionVariants)
                    .containsOnly(*shouldContain.toTypedArray())
        } else {
            assertThat(completionVariants)
                    .contains(*shouldContain.toTypedArray())
        }
    }

    override fun shouldContain(variants: List<String>, strict: Boolean) {
        this.shouldContain = variants
        this.shouldContainStrict = strict
    }

    override fun basic() {
        completionType = CompletionType.BASIC
    }

    override fun smart() {
        completionType = CompletionType.SMART
    }

}