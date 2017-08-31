package com.aemtools.lang.reppolicy.psi.pattern

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.XmlFilePattern
import com.intellij.patterns.XmlPatterns.psiElement
import com.intellij.patterns.XmlPatterns.xmlFile
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTokenType

/**
 * @author Dmytro Troynikov
 */
object RepPolicyPatterns {

    /**
     * Will match elements in `_rep_policy.xml` files.
     */
    val inRepPolicyFile: XmlFilePattern.Capture =
            xmlFile().withName("_rep_policy.xml")

    /**
     * Will match attribute name in rep policy file.
     */
    val attributeName: ElementPattern<PsiElement> =
            psiElement(XmlTokenType.XML_NAME)
                    .inside(inRepPolicyFile)

}