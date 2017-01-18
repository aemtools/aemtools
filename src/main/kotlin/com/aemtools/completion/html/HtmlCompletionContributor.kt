package com.aemtools.completion.html

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.XmlPatterns.xmlAttribute
import com.intellij.psi.xml.XmlTokenType

/**
 * Completion contributor for HTML related completions.
 * @author Dmytro_Troynikov
 */
class HtmlCompletionContributor : CompletionContributor() { init {
    extend(CompletionType.BASIC,
            psiElement(),
            htmlDataSlyUseCompletionProvider)
    extend(CompletionType.BASIC,
            psiElement(XmlTokenType.XML_NAME).inside(xmlAttribute()),
            HtmlAttributeCompletionProvider)
}
}