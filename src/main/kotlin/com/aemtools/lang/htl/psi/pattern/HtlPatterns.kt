package com.aemtools.lang.htl.psi.pattern

import com.aemtools.lang.htl.psi.HtlTypes.*
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Troynikov
 */
object HtlPatterns {

    /**
     * Matches the following:
     *
     * ```
     *    ${@ <caret>}
     * ```
     */
    val optionName: ElementPattern<PsiElement> =
            psiElement()
                    .withParent(psiElement(VARIABLE_NAME).withParent(psiElement(CONTEXT_EXPRESSION)))

    /**
     * Matches the following:
     *
     * ```
     *    ${<caret>}
     *    ${@ option=<caret>
     *    ${ [<caret>, ...] }
     *    ${variable[<caret>]}
     * ```
     */
    val variableName: ElementPattern<PsiElement> =
            or<PsiElement>(
                    psiElement(VAR_NAME)
                            .andNot(psiElement().inside(psiElement(ACCESS_IDENTIFIER)))
                            .andNot(optionName),
                    psiElement(VAR_NAME)
                            .inside(psiElement(ARRAY_LIKE_ACCESS))
                            .andNot(optionName)
            )

    /**
     * Matches the following:
     *
     * ```
     *    ${'<caret'}
     *    ${"<caret>"}
     *    ${@ option='<caret>'}
     * ```
     */
    val stringLiteralValue: ElementPattern<PsiElement> =
            psiElement().inside(psiElement(STRING_LITERAL))

    /**
     * Matches the following:
     *
     * ```
     *    ${@ context='<caret>'}
     * ```
     */
    val contextOptionAssignment: ElementPattern<PsiElement> =
            and(
                    stringLiteralValue,
                    psiElement().inside(psiElement(CONTEXT_EXPRESSION)),
                    psiElement().inside(
                            psiElement(ASSIGNMENT_VALUE)
                                    .afterSibling(psiElement(VARIABLE_NAME).withText("context"))
                    )
            )

    /**
     * Matches the following:
     *
     * ```
     *    ${object.<caret>}
     * ```
     */
    val memberAccess: ElementPattern<PsiElement> =
            or(
                    psiElement().inside(psiElement(STRING_LITERAL))
                            .inside(psiElement(ARRAY_LIKE_ACCESS)),
                    psiElement(VAR_NAME).inside(psiElement(ACCESS_IDENTIFIER))
            )

}