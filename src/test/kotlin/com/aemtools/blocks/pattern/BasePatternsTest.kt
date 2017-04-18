package com.aemtools.blocks.pattern

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.base.model.fixture.ITestFixture
import com.aemtools.completion.util.getHtmlFile
import com.aemtools.constant.const
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

/**
 * @author Dmytro_Troynikov
 */
abstract class BasePatternsTest : BaseLightTest() {

    fun testPattern(pattern: ElementPattern<PsiElement>,
                    text: String,
                    result: Boolean,
                    addCompletionPlaceholder: Boolean = true,
                    fixtureSetup: ITestFixture.(textToAdd: String) -> Unit) = fileCase {
        val textToAdd = if (addCompletionPlaceholder) {
            text.addIdeaPlaceholder()
        } else {
            text
        }
        fixtureSetup.invoke(this, textToAdd)
        verify {
            assertEquals(
                    assertionMessage(pattern, file, text),
                    result,
                    pattern.accepts(elementUnderCaret()))
        }
    }

    fun assertionMessage(pattern: ElementPattern<PsiElement>,
                         file: PsiFile,
                         text: String): String {
        val builder = StringBuilder()
        with(builder) {
            append("\nPattern:\n$pattern")
            append("\nPSI:\n${DebugUtil.psiToString(file, true)}")
            val htmlFile = file.getHtmlFile()
            if (htmlFile != null) {
                append("PSI Html:\n${DebugUtil.psiToString(htmlFile, true)}")
            }
            append("Text: $text")
        }
        return builder.toString()
    }

    private fun String.addIdeaPlaceholder(): String {
        return StringBuilder(this)
                .insert(this.indexOf(CodeInsightTestFixture.CARET_MARKER) + CodeInsightTestFixture.CARET_MARKER.length, const.IDEA_STRING_CARET_PLACEHOLDER)
                .toString()
    }

}