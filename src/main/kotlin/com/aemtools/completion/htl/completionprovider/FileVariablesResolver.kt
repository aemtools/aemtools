package com.aemtools.completion.htl.completionprovider

import com.aemtools.completion.htl.model.HtlVariableDeclaration
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.htl.predefined.HtlELPredefined.DATA_SLY_LIST_REPEAT_LIST_FIELDS
import com.aemtools.completion.util.*
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.aemtools.lang.htl.psi.util.isNotPartOf
import com.aemtools.lang.htl.psi.util.isPartOf
import com.aemtools.lang.htl.psi.util.isWithin
import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.StdLanguages
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectUtil
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import java.util.*

/**
 * Resolves variables declared within htl (html) file.
 * @author Dmytro Troynikov
 */
object FileVariablesResolver {

    fun resolveVariable(variable: VariableNameMixin): ResolutionResult {
        val project = variable.project

        val variables: List<FileVariable> = find(variable.containingFile, project)

        val foundVariable = variables.find {
            it.name == variable.variableName()
        } ?: return ResolutionResult()

        if (foundVariable.declarationType == DATA_SLY_REPEAT || foundVariable.declarationType == DATA_SLY_LIST) {
            if (foundVariable.name.endsWith("List")) {
                return ResolutionResult(null, DATA_SLY_LIST_REPEAT_LIST_FIELDS)
            } else {
                return resolveListVariable(foundVariable)
            }
        }
        if (foundVariable.type != null) {
            return ResolutionResult(JavaSearch.findClass(foundVariable.type, project))
        } else {
            return ResolutionResult()
        }
    }

    private fun resolveListVariable(fileVariable: FileVariable): ResolutionResult {
        val element = fileVariable.holderElement as XmlAttribute
        val valueElement = element.valueElement ?: return ResolutionResult()

        val htlPsi = element.containingFile.viewProvider.getPsi(HtlLanguage)

        val el = htlPsi.findElementAt(valueElement.textOffset)
        val htlHtl = el.findParentByType(HtlHtlEl::class.java)

        val propertyAccess = htlHtl.findChildrenByType(PropertyAccessMixin::class.java).first()

        return propertyAccess.resolveIterable()
    }

    /**
     * Find declaration of variable in file.
     * @param variableName the name of variable
     * @param file the file to look in
     * @return the declaration element
     */
    fun findDeclaration(variableName: String, file: PsiFile): HtlVariableDeclaration? {
        val htmlFile = file.getHtmlFile() ?: return null
        val xmlAttributes = htmlFile.findChildrenByType(XmlAttribute::class.java)
        val elements = xmlAttributes.htlAttributes()

        // TODO: handle case when multiple variables with single name declared in single file
        return elements.extractDeclarations().find { it.variableName == variableName }
    }

    fun findVariableClass(name: String, parameters: CompletionParameters): PsiClass? {
        val canonicalFile = (parameters.editor as EditorImpl).virtualFile.canonicalFile ?: return null
        val project = ProjectUtil.guessProjectForContentFile(canonicalFile) ?: return null

        val variables = find(parameters.originalFile, project)
        val result = variables.find { it.name == name } ?: return null
        if (result.type == null) {
            return null
        }

        return JavaSearch.findClass(result.type, project)
    }

    /**
     * Collect [LookupElement] list from elements which applicable for given position.
     * @param position location against which lookup elements should be filtered
     * @param completionParameters the completion parameters
     * @return list of lookup elements
     */
    fun findForPosition(position: PsiElement, completionParameters: CompletionParameters)
            : List<LookupElement> {
        val htlFile = completionParameters.originalFile
        val htmlFile = htlFile.viewProvider.getPsi(StdLanguages.HTML)

        val attributes: Collection<XmlAttribute> = PsiTreeUtil.findChildrenOfType(htmlFile, XmlAttribute::class.java)
        val result = ArrayList<LookupElement>()
        attributes.forEach {
            with(it.name) {
                when {
                    (startsWith(DATA_SLY_USE) && length > DATA_SLY_USE.length)
                            || (startsWith(DATA_SLY_TEST) && length > DATA_SLY_TEST.length) -> {
                        val variableName = substring(lastIndexOf(".") + 1)
                        val varClass = it.resolveUseClass()
                        result += LookupElementBuilder.create(variableName)
                                .withTypeText(varClass)
                    }
                    startsWith(DATA_SLY_LIST) -> {
                        val (itemName, itemListName) = extractItemAndItemListNames(this)

                        val tag = it.findParentByType(XmlTag::class.java) ?: return result

                        if (position.isWithin(tag)) {
                            result.add(LookupElementBuilder.create(itemName))
                            result.add(LookupElementBuilder.create(itemListName))
                        } else {
                        }
                    }
                    startsWith(DATA_SLY_REPEAT) -> {
                        val (itemName, itemListName) = extractItemAndItemListNames(this)

                        val tag = it.findParentByType(XmlTag::class.java) ?: return result

                        if (position.isPartOf(tag) && position.isNotPartOf(it)) {
                            result.add(LookupElementBuilder.create(itemName))
                            result.add(LookupElementBuilder.create(itemListName))
                        } else {

                        }
                    }
                    startsWith(DATA_SLY_TEMPLATE) -> {
                        val tag = it.findParentByType(XmlTag::class.java)
                                ?: return@forEach

                        if (position.isWithin(tag)) {
                            val templateParameters = it.extractTemplateParameters()

                            templateParameters.forEach {
                                result.add(LookupElementBuilder.create(it)
                                        .withTypeText("Template parameter"))
                            }
                        } else {
                        }
                    }
                    else -> {
                    }
                }
            }

        }

        return result
    }

    /**
     * Find file variables from current file.
     */
    fun find(psiFile: PsiFile, project: Project): List<FileVariable> {
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