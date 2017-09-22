package com.aemtools.completion.htl.common

import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.aemtools.completion.util.extractDeclarations
import com.aemtools.completion.util.filterForPosition
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.getHtlFile
import com.aemtools.completion.util.getHtmlFile
import com.aemtools.completion.util.htlAttributes
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.lang.StdLanguages
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute

/**
 * Resolves variables declared within htl (html) file.
 *
 * @author Dmytro Troynikov
 */
object FileVariablesResolver {

  /**
   * Find declaration of variable in file.
   * @param variableName the name of variable
   * @param element the element
   * @return the declaration element
   */
  fun findDeclaration(variableName: String,
                      element: PsiElement): HtlVariableDeclaration? {
    val htmlFile = element.containingFile.getHtmlFile() ?: return null
    val xmlAttributes = htmlFile.findChildrenByType(XmlAttribute::class.java)
        .toList()

    val elements = xmlAttributes.htlAttributes()

    val result = elements.extractDeclarations()
        .filterForPosition(element)
        .find { it.variableName == variableName }

    return result
  }

  /**
   * Check if given variable is valid in given position.
   *
   * @param variableName variable name
   * @param position position to start variable lookup
   * @return *true* if variable with given name is valid in given context
   */
  fun validVariable(variableName: String,
                    position: PsiElement): Boolean =
      findDeclaration(variableName, position) != null

  /**
   * Collect [HtlVariableDeclaration] objects suitable for given position.
   * @param position location against which variables should be filtered
   * @param completionParameters the completion parameters
   * @return list of htl variable declarations
   */
  fun declarationsForPosition(position: PsiElement, completionParameters: CompletionParameters)
      : List<HtlVariableDeclaration> {
    val htlFile = completionParameters.originalFile
    val htmlFile = htlFile.viewProvider.getPsi(StdLanguages.HTML)

    val attributes: List<XmlAttribute> = PsiTreeUtil.findChildrenOfType(htmlFile, XmlAttribute::class.java)
        .toList()

    return attributes.extractDeclarations()
        .filterForPosition(position).toList()
  }

  /**
   * Collect [HtlVariableDeclaration] object suitable for given position.
   * @param position location against which variables should be filtered
   * @return list of htl variable declarations
   */
  fun declarationsForPosition(position: PsiElement): List<HtlVariableDeclaration> {
    val htlFile = position.containingFile.originalFile.getHtlFile()
        ?: return emptyList()
    val htmlFile = htlFile.getHtmlFile()
        ?: return emptyList()

    val attributes: List<XmlAttribute> = PsiTreeUtil.findChildrenOfType(htmlFile, XmlAttribute::class.java)
        .toList()

    return attributes.extractDeclarations()
        .filterForPosition(position).toList()
  }

}
