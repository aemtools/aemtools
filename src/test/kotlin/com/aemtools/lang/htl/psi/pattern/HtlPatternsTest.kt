package com.aemtools.lang.htl.psi.pattern

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.completion.util.getHtmlFile
import com.aemtools.constant.const.IDEA_STRING_CARET_PLACEHOLDER
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.contextOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyIncludeNoEl
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseNoEl
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.htlAttribute
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.memberAccess
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.optionName
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.stringLiteralValue
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.variableName
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.fixtures.CodeInsightTestFixture.CARET_MARKER

/**
 * @author Dmytro Troynikov
 */
class HtlPatternsTest : BaseLightTest() {

    fun testVariablePatternMain() = testHtlPattern(
            variableName,
            "$DOLLAR{$CARET}",
            true
    )

    fun testVariablePatternAsOptionValue() = testHtlPattern(
            variableName,
            "$DOLLAR{@ option=$CARET}",
            true
    )

    fun testVariablePatternAsPartOfArray() = testHtlPattern(
            variableName,
            "$DOLLAR{[$CARET, 'string']}",
            true
    )

    fun testVariablePatternInArrayLikeAccess() = testHtlPattern(
            variableName,
            "$DOLLAR{variable[$CARET]}",
            true
    )

    fun testVariablePatternShouldNotMatchAccessIdentifier() = testHtlPattern(
            variableName,
            "$DOLLAR{variable.$CARET}",
            false
    )

    fun testVariablePatternShouldNotMatchOption() = testHtlPattern(
            variableName,
            "$DOLLAR{@ $CARET}",
            false
    )

    fun testStringLiteralValueMain() = testHtlPattern(
            stringLiteralValue,
            "$DOLLAR{'$CARET'}",
            true
    )

    fun testStringLiteralValueInOptions() = testHtlPattern(
            stringLiteralValue,
            "$DOLLAR{@ option='$CARET'}",
            true
    )

    fun testOptionNameMain() = testHtlPattern(
            optionName,
            "$DOLLAR{@ $CARET}",
            true
    )

    fun testOptionNameSecondOption() = testHtlPattern(
            optionName,
            "$DOLLAR{@ option='', $CARET}",
            true
    )

    fun testOptionNameWithValue() = testHtlPattern(
            optionName,
            "$DOLLAR{@ $CARET=''",
            true
    )

    fun testOptionNameOptionValueShouldNotMatch() = testHtlPattern(
            optionName,
            "$DOLLAR{@ option=$CARET}",
            false
    )

    fun testContextOptionAssignmentMain() = testHtlPattern(
            contextOptionAssignment,
            "$DOLLAR{@ context='$CARET'}",
            true
    )

    fun testContextOptionAssignmentShouldTriggerOnUnknownOption() = testHtlPattern(
            contextOptionAssignment,
            "$DOLLAR{@ join='$CARET'}",
            false
    )

    fun testMemberAccessMain() = testHtlPattern(
            memberAccess,
            "$DOLLAR{object.$CARET}",
            true
    )

    fun testMemberAccessArrayLikeAccess() = testHtlPattern(
            memberAccess,
            "$DOLLAR{object['$CARET']}",
            true
    )

    fun testMemberAccessInArrayLikeAccess() = testHtlPattern(
            memberAccess,
            "$DOLLAR{object[inner.$CARET]}",
            true
    )

    fun testMemberAccessInOptions() = testHtlPattern(
            memberAccess,
            "$DOLLAR{@ option=object.$CARET}",
            true
    )

    fun testMemberAccessShouldMatchVariable() = testHtlPattern(
            memberAccess,
            "$DOLLAR{$CARET}",
            false
    )

    fun testDataSlyUseNoElMain() = testHtlPattern(
            dataSlyUseNoEl,
            """<div data-sly-use="$CARET">""",
            true
    )

    fun testDataSlyUseNoElMain2() = testHtlPattern(
            dataSlyUseNoEl,
            """<div data-sly-use.bean="$CARET">""",
            true
    )

    fun testDataSlyIncludeNoElMain() = testHtlPattern(
            dataSlyIncludeNoEl,
            """<div data-sly-include="$CARET">""",
            true
    )

    fun testHtlAttributeMain() = testHtlPattern(
            htlAttribute,
            "<div ${CARET}data-sly-use>",
            true,
            false
    )

    fun testHtlPattern(pattern: ElementPattern<PsiElement>,
                       text: String,
                       result: Boolean,
                       addCompletionPlaceholder: Boolean = true) = fileCase {
        val textToAdd = if (addCompletionPlaceholder) {
            text.addIdeaPlaceholder()
        } else {
            text
        }
        addHtml("test.html", textToAdd)
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
                .insert(this.indexOf(CARET_MARKER) + CARET_MARKER.length, IDEA_STRING_CARET_PLACEHOLDER)
                .toString()
    }

}