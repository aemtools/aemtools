package com.aemtools.lang.htl.psi.pattern

import com.aemtools.blocks.pattern.BasePatternsTest
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.contextOptionAssignment
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyCallOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyIncludeMainString
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyIncludeNoEl
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyTemplateOption
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseMainString
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.dataSlyUseNoEl
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.htlAttribute
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.mainVariableInsideOfDataSlyCall
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.memberAccess
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.optionName
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.stringLiteralValue
import com.aemtools.lang.htl.psi.pattern.HtlPatterns.variableName
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
class HtlPatternsTest : BasePatternsTest() {

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

    fun testDataSlyCallOptionMain() = testHtlPattern(
            dataSlyCallOption,
            """
                <div data-sly-call="$DOLLAR{template @ $CARET}"></div>
            """,
            true
    )

    fun testDataSlyCallOptionShouldMatchOrdinaryOption() = testHtlPattern(
            dataSlyCallOption,
            """
                $DOLLAR{@ $CARET}
            """,
            false
    )

    fun testDataSlyTemplateOptionMain() = testHtlPattern(
            dataSlyTemplateOption,
            """
                <div data-sly-template.template="$DOLLAR{@ $CARET}"></div>
            """,
            true
    )

    fun testDataSlyTemplateOptionShouldMatchOrdinaryOption() = testHtlPattern(
            dataSlyTemplateOption,
            "$DOLLAR{@ $CARET}",
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

    fun testMainVariableInsideOFDataSlyCallMain() = testHtlPattern(
            mainVariableInsideOfDataSlyCall,
            """
                <div data-sly-call="$DOLLAR{$CARET}"></div>
            """,
            true
    )

    fun testMainVariableInsideOfDataSlyCallShouldNotMatchInOption() = testHtlPattern(
            mainVariableInsideOfDataSlyCall,
            """
                <div data-sly-call="$DOLLAR{@ $CARET}"></div>
            """,
            false
    )

    fun testMainVariableInsideOfDataSlyCallShouldNotMatchInOption2() = testHtlPattern(
            mainVariableInsideOfDataSlyCall,
            """
                <div data-sly-call="$DOLLAR{@ option=$CARET}"></div
            """,
            false
    )

    fun testSlyIncludeMainStringMain() = testHtlPattern(
            dataSlyIncludeMainString,
            """
                <div data-sly-include="$DOLLAR{'$CARET'}"></div>
            """,
            true
    )

    fun testSlyIncludeMainStringShouldNotMatchOtherAttribute() = testHtlPattern(
            dataSlyIncludeMainString,
            """
                <div data-sly-use="$DOLLAR{'$CARET'}"></div>
            """,
            false
    )

    fun testDataSlyUseMainStringMain() = testHtlPattern(
            dataSlyUseMainString,
            """
                <div data-sly-use="$DOLLAR{'$CARET'}"></div>
            """,
            true
    )

    fun testDataSlyUseMainStringShouldNotMatchOtherAttribute() = testHtlPattern(
            dataSlyUseMainString,
            """
                <div data-sly-include="$DOLLAR{'$CARET'}></div>
            """,
            false
    )

    private fun testHtlPattern(
            pattern: ElementPattern<PsiElement>,
            text: String,
            result: Boolean,
            addCompletionPlaceholder: Boolean = true) =
            testPattern(pattern,
                    text,
                    result,
                    addCompletionPlaceholder,
                    { textToAdd -> addHtml("test.html", textToAdd) })

}