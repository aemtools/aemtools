package com.aemtools.completion.util

import com.aemtools.completion.htl.model.DeclarationType
import com.aemtools.completion.htl.model.HtlVariableDeclaration
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.completion.htl.predefined.HtlELPredefined
import com.aemtools.constant.const
import com.aemtools.constant.const.SLY_TAG
import com.aemtools.constant.const.htl.DATA_SLY_ATTRIBUTE
import com.aemtools.constant.const.htl.DATA_SLY_CALL
import com.aemtools.constant.const.htl.DATA_SLY_ELEMENT
import com.aemtools.constant.const.htl.DATA_SLY_INCLUDE
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_RESOURCE
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_TEXT
import com.aemtools.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.constant.const.htl.UNIQUE_HTL_ATTRIBUTES
import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlVariableName
import com.intellij.openapi.util.Conditions
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.xml.XmlTokenImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag

/**
 * @author Dmytro_Troynikov
 */
object PsiXmlUtil {

    /**
     * Extracts the tag containing given element (itself in case if passed element is tag itself)
     * @param element the starting point of lookup
     * @return tag container *null* if no tag found
     */
    fun extractTag(element: PsiElement): XmlTag? {
        var currentElement: PsiElement? = element
        while (currentElement != null) {
            if (currentElement is XmlTag) {
                return currentElement
            }
            currentElement = currentElement.parent
        }
        return null
    }

    /**
     * Removes Idea placeholder which denotes the position of caret.
     *
     *
     * __Example input:__
     *
     *
     *    "pathfIntellijIdeaRulezzzield" -> "pathfield"
     *
     *
     *    "IntellijIdeaRulezzz" -> ""
     * @param input string to process.
     * @return the input string without caret placeholder.
     */
    fun removeCaretPlaceholder(input: String?): String? {
        if (input == null) {
            return null
        }

        if (input.contains(const.IDEA_STRING_CARET_PLACEHOLDER)) {
            return input.substring(0, input.indexOf(const.IDEA_STRING_CARET_PLACEHOLDER))
        }

        return input
    }

    /**
     * Extract name of attribute associated with given PsiElement
     */
    fun nameOfAttribute(element: PsiElement?): String? {
        if (element == null) {
            return null
        }

        var attr: PsiElement? = element.findParentByType(XmlAttribute::class.java) ?: return null

        return (attr as XmlAttribute).name
    }

}

/**
 * Searches for children by type (@see [PsiTreeUtil])
 */
fun <T : PsiElement> PsiElement?.findChildrenByType(type: Class<T>): Collection<T> {
    return PsiTreeUtil.findChildrenOfType(this, type)
}

/**
 * Searches for parent PSI element of specified class
 */
fun <T : PsiElement> PsiElement?.findParentByType(type: Class<T>): T? {
    return PsiTreeUtil.findFirstParent(this, Conditions.instanceOf(type)) as T?
}

/**
 * Check if current [PsiElement] has parent of specified class.
 */
fun <T : PsiElement> PsiElement?.hasParent(type: Class<T>): Boolean {
    return this.findParentByType(type) != null
}

/**
 * Extract Htl unique attributes as [Collection<String>] from given [XmlAttribute] collection.
 */
fun Array<PsiElement>.uniqueHtlAttributes(): Collection<String> =
        filter { it.isUniqueHtlAttribute() }.map { it.text }

/**
 * Check if current [PsiElement] is unique. Unique attributes are
 *  `data-sly-unwrap`
 *  `data-sly-list`
 */
fun PsiElement.isUniqueHtlAttribute(): Boolean = UNIQUE_HTL_ATTRIBUTES.contains(this.text)

fun XmlTag.isSlyTag(): Boolean = this.name == SLY_TAG

/**
 * Get element type String.
 */
fun PsiElement?.elementType(): String? = (this as? XmlTokenImpl)?.elementType.toString() ?: null

/**
 * Extract [HtlHtlEl] from current element.
 */
fun XmlAttribute.extractHtlHel(): HtlHtlEl? {
    val htlFile = containingFile.viewProvider.getPsi(HtlLanguage)
    val valueElement = valueElement ?: return null
    val helStart = htlFile.findElementAt(valueElement.textOffset + 1)
    return helStart.findParentByType(HtlHtlEl::class.java)
}

/**
 * Extract Htl attributes from given [XmlAttribute] collection.
 */
fun Collection<XmlAttribute>.htlAttributes(): Collection<XmlAttribute> =
        filter { it.isHtlAttribute() }

/**
 * Resolves the class of variable declared in current [XmlAttribute] element.
 *
 * e.g.
 *
 * ```
 * data-sly-use.bean="com.my.package.BeanClass" -> "com.my.package.BeanClass
 * data-sly-use.bean="${'com.my.package.BeanClass' @ param1='value'}" -> "com.my.package.BeanClass"
 *
 * TODO: find solution for:
 * data-sly-use.bean="javascript-file.js"
 * ->
 * ???
 * ```
 *
 * @return full qualified class name, or _null_ in case if resolution is not possible
 */
fun XmlAttribute.resolveUseClass(): String? {
    val attributeValue = valueElement?.value ?: return null
    return when {
        attributeValue.matches(Regex("[\\w.]+")) -> attributeValue
        attributeValue.indexOf("\${") != -1 -> extractBeanNameFromEl(attributeValue)
        else -> null
    }
}

private fun extractBeanNameFromEl(el: String): String? {
    val start = el.indexOf("'") + 1
    val end = el.indexOf("'", start + 1)
    if (start != -1 && end != -1) {
        return el.substring(start, end)
    }
    return null
}

/**
 * Check if current [XmlAttribute] is `data-sly-use` attribute.
 */
fun XmlAttribute.isDataSlyUse(): Boolean = this.name.startsWith(DATA_SLY_USE)

/**
 * Check if current [XmlAttribute] is Htl attribute.
 *
 * @return __true__ if current element is Htl attribute
 */
fun XmlAttribute.isHtlAttribute(): Boolean = with(this.name) {
    when {
        startsWith(DATA_SLY_USE)
                || startsWith(DATA_SLY_TEST)
                || startsWith(DATA_SLY_REPEAT)
                || startsWith(DATA_SLY_LIST)
                || startsWith(DATA_SLY_ATTRIBUTE)
                || startsWith(DATA_SLY_ELEMENT)
                || startsWith(DATA_SLY_CALL)
                || startsWith(DATA_SLY_INCLUDE)
                || startsWith(DATA_SLY_UNWRAP)
                || startsWith(DATA_SLY_TEXT)
                || startsWith(DATA_SLY_RESOURCE) -> true
        else -> false
    }
}


/**
 * Check if current element is Htl attribute which declares some variable.
 *
 * e.g.
 *
 * ```
 * data-sly-use.bean -> true
 * data-sly-use -> false
 * data-sly-list -> true
 * ```
 * @return __true__ if current element declares some variable
 */
fun XmlAttribute.isHtlDeclarationAttribute(): Boolean =
        with(this.name) {
            when {
                startsWith(DATA_SLY_USE) && length > DATA_SLY_USE.length -> true
                startsWith(DATA_SLY_TEST) && length > DATA_SLY_TEST.length -> true
                startsWith(DATA_SLY_LIST) -> true
                startsWith(DATA_SLY_REPEAT) -> true
                else -> false
            }
        }

/**
 * Extract list of Htl variable declarations from current [XmlAttribute] collection.
 * @return collection of [HtlVariableDeclaration] elements
 */
fun Collection<XmlAttribute>.extractDeclarations(): Collection<HtlVariableDeclaration> {
    return filter { it.isHtlDeclarationAttribute() }
            .flatMap {
                with(it.name) {
                    when {
                        startsWith(DATA_SLY_USE) || startsWith(DATA_SLY_TEST) ->
                            listOf(HtlVariableDeclaration(it, substring(lastIndexOf(".") + 1)))

                        startsWith(DATA_SLY_LIST) || startsWith(DATA_SLY_REPEAT) -> {
                            val (item, itemList) = extractItemAndItemListNames(this)

                            listOf(
                                    HtlVariableDeclaration(it, item, DeclarationType.ITERABLE),
                                    HtlVariableDeclaration(it,
                                            itemList,
                                            DeclarationType.VARIABLE,
                                            ResolutionResult(
                                                    predefined = HtlELPredefined.DATA_SLY_LIST_REPEAT_LIST_FIELDS))
                            )
                        }
                        else -> listOf()
                    }
                }
            }
}

/**
 * Extract template parameters from current [XmlAttribute]
 * e.g.
 *
 * ```
 *   data-sly-template.myTemplate="${@ param1, param2}"
 * ```
 *
 * will return list of "param1" and "param2".
 *
 * @return list of parameter names, empty list if no parameters were declared
 * or in case if the attribute is not `data-sly-template`
 */
fun XmlAttribute.extractTemplateParameters(): List<String> {
    if (!this.name.startsWith(DATA_SLY_TEMPLATE)) {
        return listOf()
    }

    val htlHel = this.extractHtlHel() ?: return listOf()

    return htlHel.findChildrenByType(HtlVariableName::class.java)
            .filter(HtlVariableName::isOption).map { it.text }
}