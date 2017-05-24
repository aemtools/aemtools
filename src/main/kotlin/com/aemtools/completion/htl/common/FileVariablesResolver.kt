package com.aemtools.completion.htl.common

import com.aemtools.completion.htl.model.declaration.HtlVariableDeclaration
import com.aemtools.completion.util.*
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.lang.StdLanguages
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import java.util.*

/**
 * Resolves variables declared within htl (html) file.
 * @author Dmytro Troynikov
 */
object FileVariablesResolver {

    /**
     * Find declaration of variable in file.
     * @param variableName the name of variable
     * @param element the element
     * @param file the file to look in
     * @return the declaration element
     */
    fun findDeclaration(variableName: String,
                        element: PsiElement,
                        file: PsiFile): HtlVariableDeclaration? {
        val htmlFile = file.getHtmlFile() ?: return null
        val xmlAttributes = htmlFile.findChildrenByType(XmlAttribute::class.java)
        val elements = xmlAttributes.htlAttributes()

        val result = elements.extractDeclarations()
                .filterForPosition(element)
                .find { it.variableName == variableName }

        return result
    }

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

        val attributes: Collection<XmlAttribute> = PsiTreeUtil.findChildrenOfType(htmlFile, XmlAttribute::class.java)
        return attributes.extractDeclarations()
                .filterForPosition(position).toList()
    }

    /**
     * Find file variables from current file.
     */
    fun find(psiFile: PsiFile): List<FileVariable> {
        val htmlFile = psiFile.viewProvider.getPsi(StdLanguages.HTML)
        val attributes = PsiTreeUtil.findChildrenOfType(htmlFile, XmlAttribute::class.java)

        val declarationAttributes = attributes.filter { it.isHtlDeclarationAttribute() }

        val result: ArrayList<FileVariable> = ArrayList<FileVariable>()

        declarationAttributes.forEach {
            val attrName = it.name

            with(attrName) {
                when {
                    startsWith(DATA_SLY_USE) -> {
                        val varName = substring(lastIndexOf(".") + 1)
                        val varClass = it.resolveUseClass()

                        result.add(FileVariable(varName, varClass, it, extractDeclarationType(it.name)))
                    }
                    startsWith(DATA_SLY_TEST) -> {
                        val varName = substring(lastIndexOf(".") + 1)
                        val varClass = resolveTestClass(it)

                        result.add(FileVariable(varName, varClass, it, extractDeclarationType(it.name)))
                    }
                    startsWith(DATA_SLY_REPEAT) || startsWith(DATA_SLY_LIST) -> {
                        val (itemName, itemListName) = extractItemAndItemListNames(this)

                        result.add(FileVariable(itemName, null, it, extractDeclarationType(it.name)))
                        result.add(FileVariable(itemListName, null, it, extractDeclarationType(it.name)))
                    }
                    else -> {
                    }
                }
            }
        }

        return result
    }

    private fun extractDeclarationType(attributeName: String): String? = with(attributeName) {
        when {
            startsWith(DATA_SLY_USE) -> DATA_SLY_USE
            startsWith(DATA_SLY_TEST) -> DATA_SLY_TEST
            startsWith(DATA_SLY_LIST) -> DATA_SLY_LIST
            startsWith(DATA_SLY_REPEAT) -> DATA_SLY_REPEAT
            else -> null
        }
    }

    /**
     * Resolve class name for __data-sly-test__ attribute.
     */
    private fun resolveTestClass(attribute: XmlAttribute): String? {
        return attribute.resolveUseClass()
    }

}

data class FileVariable(val name: String,
                        val type: String? = null,
                        val holderElement: PsiElement,
                        val declarationType: String?)