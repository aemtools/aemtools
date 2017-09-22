package com.aemtools.lang.htl.psi.pattern

import com.aemtools.constant.const
import com.aemtools.constant.const.htl.DATA_SLY_CALL
import com.aemtools.constant.const.htl.DATA_SLY_INCLUDE
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_RESOURCE
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.constant.const.htl.HTL_ATTRIBUTES
import com.aemtools.lang.htl.psi.HtlExpression
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlTypes.ACCESS_IDENTIFIER
import com.aemtools.lang.htl.psi.HtlTypes.ARRAY_LIKE_ACCESS
import com.aemtools.lang.htl.psi.HtlTypes.ASSIGNMENT
import com.aemtools.lang.htl.psi.HtlTypes.ASSIGNMENT_VALUE
import com.aemtools.lang.htl.psi.HtlTypes.CONTEXT_EXPRESSION
import com.aemtools.lang.htl.psi.HtlTypes.EL_START
import com.aemtools.lang.htl.psi.HtlTypes.STRING_LITERAL
import com.aemtools.lang.htl.psi.HtlTypes.VARIABLE_NAME
import com.aemtools.lang.htl.psi.HtlTypes.VAR_NAME
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.and
import com.intellij.patterns.PlatformPatterns.or
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PlatformPatterns.psiFile
import com.intellij.patterns.PlatformPatterns.string
import com.intellij.patterns.XmlPatterns.xmlAttribute
import com.intellij.patterns.XmlPatterns.xmlAttributeValue
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.xml.XmlTokenType.XML_NAME

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
      or(
          psiElement()
              .withParent(psiElement(VARIABLE_NAME)
                  .withParent(psiElement(CONTEXT_EXPRESSION))),
          psiElement()
              .withParent(psiElement(VARIABLE_NAME)
                  .withParent(psiElement(ASSIGNMENT)
                      .withParent(psiElement(CONTEXT_EXPRESSION))))
      )

  /**
   * Matches option inside of data-sly-call, e.g.:
   *
   * ```
   *  <div data-sly-call="${@ <caret>}"
   * ```
   */
  val dataSlyCallOption: ElementPattern<PsiElement> =
      and(
          optionName,
          psiElement()
              .inside(psiElement()
                  .with(HtlTemplatePattern(DATA_SLY_CALL)))
      )

  /**
   * Matches option inside of data-sly-template, e.g.:
   *
   * ```
   *  <div data-sly-template="${@ <caret>}"
   * ```
   */
  val dataSlyTemplateOption: ElementPattern<PsiElement> =
      optionInsideAttribute(DATA_SLY_TEMPLATE)

  /**
   * Matches option inside of data-sly-resource, e.g.:
   *
   * ```
   *  <div data-sly-resource="${@ <caret>}"
   * ```
   */
  val dataSlyResourceOption: ElementPattern<PsiElement> =
      optionInsideAttribute(DATA_SLY_RESOURCE)

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
      namedOptionAssignement(const.htl.options.CONTEXT)

  /**
   * Matches the following:
   *
   * ```
   *    ${@ resourceType='<caret>'}
   * ```
   */
  val resourceTypeOptionAssignment: ElementPattern<PsiElement> =
      namedOptionAssignement(const.htl.options.RESOURCE_TYPE)

  /**
   * Create matcher for assignment of option with given name.
   * e.g.:
   *
   * ```
   * namedOptionAssignement("context") ->
   *   will create pattern that will match:
   *   ${@ context='<caret>'}
   * ```
   *
   * @param option option name
   * @return new element pattern
   */
  private fun namedOptionAssignement(option: String): ElementPattern<PsiElement> =
      and(
          stringLiteralValue,
          psiElement().inside(psiElement(CONTEXT_EXPRESSION)),
          psiElement().inside(
              psiElement(ASSIGNMENT_VALUE)
                  .afterSibling(psiElement(VARIABLE_NAME).withText(option))
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

  /**
   * Matches the following:
   *
   * ```
   *    data-sly-use="<caret>"
   *    data-sly-use.bean="<caret>"
   * ```
   */
  val dataSlyUseNoEl: ElementPattern<PsiElement> =
      psiElement()
          .inside(xmlAttributeValue().withLocalName(
              or(
                  string().equalTo(DATA_SLY_USE),
                  string().startsWith("$DATA_SLY_USE.")
              )
          ))
          .inFile(psiFile().with(HtlFilePattern))

  /**
   * Matches the following:
   *
   * ```
   *    data-sly-include="<caret>"
   * ```
   */
  val dataSlyIncludeNoEl: ElementPattern<PsiElement> =
      psiElement()
          .inside(xmlAttributeValue()
              .withLocalName(string().equalTo(DATA_SLY_INCLUDE)))
          .inFile(psiFile().with(HtlFilePattern))

  /**
   * Matches Htl xml attribute
   */
  val htlAttribute: ElementPattern<PsiElement> =
      psiElement(XML_NAME).withParent(xmlAttribute().withName(
          or(
              string().oneOfIgnoreCase(*HTL_ATTRIBUTES.toTypedArray()),
              string().startsWith("$DATA_SLY_USE."),
              string().startsWith("$DATA_SLY_TEST."),
              string().startsWith("$DATA_SLY_LIST."),
              string().startsWith("$DATA_SLY_REPEAT."),
              string().startsWith("$DATA_SLY_TEMPLATE.")
          )
      ))

  /**
   * Matches the following:
   *
   * ```
   *    data-sly-call="${<caret>}"
   * ```
   */
  val mainVariableInsideOfDataSlyCall: ElementPattern<PsiElement> =
      psiElement().inside(psiElement(HtlExpression::class.java))
          .afterLeafSkipping(
              psiElement(TokenType.WHITE_SPACE),
              psiElement(EL_START))
          .inside(psiElement().with(HtlTemplatePattern(DATA_SLY_CALL)))

  /**
   * Matches the following:
   *
   * ```
   *    data-sly-include="${'<caret>'}"
   * ```
   */
  val dataSlyIncludeMainString: ElementPattern<PsiElement> =
      mainStringInAttribute(DATA_SLY_INCLUDE)

  /**
   * Matches the following:
   *
   * ```
   *    data-sly-use="${'<caret>'}"
   *    data-sly-use.bean="${'<caret>'}"
   * ```
   */
  val dataSlyUseMainString: ElementPattern<PsiElement> =
      mainStringInAttribute(DATA_SLY_USE)

  /**
   * Matches the following:
   *
   * ```
   *    ${'<caret' @ i18n}
   * ```
   */
  val localizationMainString: ElementPattern<PsiElement> =
      and(
          stringLiteralValue,
          psiElement().afterLeafSkipping(
              psiElement(TokenType.WHITE_SPACE),
              psiElement(EL_START)),
          psiElement().withAncestor(7,
              psiElement(HtlHtlEl::class.java)
                  .withChild(psiElement()
                      .withText(const.htl.options.I18N))
          )
      )

  /**
   * Create pattern which will match main string in given htl attribute.
   *
   * @param attribute the name of attribute
   * @return new element pattern
   */
  private fun mainStringInAttribute(attribute: String): ElementPattern<PsiElement> =
      and(
          stringLiteralValue,
          psiElement().afterLeafSkipping(
              psiElement(TokenType.WHITE_SPACE),
              psiElement(EL_START))
              .inside(psiElement().with(HtlTemplatePattern(attribute)))
      )

  /**
   * Create pattern which will match option name inside of given htl attribute.
   *
   * @param attribute the name of attribute
   * @return new element pattern
   */
  private fun optionInsideAttribute(attribute: String): ElementPattern<PsiElement> =
      and(
          optionName,
          psiElement()
              .inside(psiElement()
                  .with(HtlTemplatePattern(attribute)))
      )

}
