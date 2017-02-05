package com.aemtools.completion.util

import com.aemtools.completion.htl.model.DeclarationAttributeType
import com.aemtools.completion.htl.model.HtlVariableDeclaration
import com.aemtools.constant.const
import com.aemtools.lang.htl.psi.*
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.aemtools.lang.htl.psi.util.isNotPartOf
import com.aemtools.lang.htl.psi.util.isPartOf
import com.aemtools.lang.htl.psi.util.isWithin
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import java.util.*

/**
 * Htl related utility methods.
 *
 * @author Dmytro_Troynikov
 */

/**
 * Extract iterable variable and list variable from given attribute name
 * e.g.
 *
 * ```
 * data-sly-list -> item to itemList
 * data-sly-repeat.overriden -> overriden to overridenList
 * ```
 * @return _iterable_ to _list variable_ names pair
 */
fun extractItemAndItemListNames(value: String): Pair<String, String> {
    val item: String = when {
        value.startsWith(const.htl.DATA_SLY_REPEAT) && value.length > const.htl.DATA_SLY_REPEAT.length + 1 -> {
            value.substring(value.lastIndexOf(".") + 1)
        }
        value.startsWith(const.htl.DATA_SLY_LIST) && value.length > const.htl.DATA_SLY_LIST.length + 1 -> {
            value.substring(value.lastIndexOf(".") + 1)
        }
        else -> {
            "item"
        }
    }
    return item to "${item}List"
}

/**
 * Check if current string literal is the main EL's string
 * (e.g. ${'main string' @ param='not main string'})
 */
fun HtlStringLiteral.isMainString(): Boolean {
    val expression = this.findParentByType(HtlExpression::class.java) ?: return false

    return expression.parent is HtlHel
}

/**
 * Check if current variable is "option"
 *
 * ```
 *  ${variable} -> false
 *  ${@ variable} -> true
 * ```
 */
fun HtlVariableName.isOption(): Boolean {
    return this.hasParent(HtlContextExpression::class.java)
            && !this.hasParent(HtlAssignment::class.java)
}

/**
 * Extract first (top level) [PropertyAccessMixin].
 */
fun HtlHtlEl.extractPropertyAccess(): PropertyAccessMixin? {
    val propertyAccessItems = findChildrenByType(PropertyAccessMixin::class.java)
    if (propertyAccessItems.isEmpty()) {
        return null
    }
    return propertyAccessItems.first()
}

/**
 * Check if current [HtlHtlEl] element resides within attribute with given name.
 * @param attributeName the name of attribute
 * @return __true__ if current element is the value of attribute with given name
 */
fun HtlHtlEl.isInsideOF(attributeName: String): Boolean {
    val html = this.containingFile.getHtmlFile()
            ?: return false
    val attribute = html.findElementAt(this.textOffset - 1)
            .findParentByType(XmlAttribute::class.java) ?: return false

    return attribute.name.startsWith(attributeName)
}

/**
 * Check if current [PsiElement] element resides within attribute with given name.
 * @param attributeName the name of attribute
 * @return __true__ if current element si the value of attribute with given name
 */
fun PsiElement.isInsideOf(attributeName: String): Boolean {
    val htlHtlEl = findParentByType(HtlHtlEl::class.java) ?: return false
    return htlHtlEl.isInsideOF(attributeName)
}

/**
 * Collection [HtlVariableDeclaration] element which are applicable for given position
 * @param position the starting position
 * @return collection of applicable declarations
 */
fun Collection<HtlVariableDeclaration>.filterForPosition(position: PsiElement): Collection<HtlVariableDeclaration> {
    val applicableDeclarations = this.filter {
        when (it.attributeType) {
            DeclarationAttributeType.DATA_SLY_USE ->
                true
            DeclarationAttributeType.DATA_SLY_TEST ->
                true
            DeclarationAttributeType.DATA_SLY_LIST -> {
                val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

                return@filter position.isWithin(tag)
            }
            DeclarationAttributeType.DATA_SLY_REPEAT -> {
                val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

                return@filter position.isPartOf(tag) && position.isNotPartOf(it.xmlAttribute)
            }
            DeclarationAttributeType.DATA_SLY_TEMPLATE -> {
                val tag = it.xmlAttribute.findParentByType(XmlTag::class.java) ?: return@filter false

                return@filter position.isWithin(tag)
            }

            else -> false
        }
    }

    val groupedByName = applicableDeclarations.groupBy { it.variableName }
    val result = if (groupedByName.values.find { it.size > 1 } != null) {

        groupedByName.values.flatMap {
            if (it.size == 1) {
                it
            } else {

                val parentTags = position.run {
                    val html = position.containingFile.getHtmlFile() ?: return@run listOf<XmlTag>()
                    val parent = position.findParentByType(HtlHtlEl::class.java) ?: return@run listOf<XmlTag>()
                    val offset = parent.textOffset - 1
                    val htmlElement = html.findElementAt(offset)

                    var currentElement = htmlElement.findParentByType(XmlTag::class.java)
                    val result = ArrayList<XmlTag>()

                    while (currentElement != null) {
                        result.add(currentElement)
                        currentElement = currentElement.prevSibling.findParentByType(XmlTag::class.java)
                    }
                    result
                }

                val closest = it.minBy {
                    val myTag = it.xmlAttribute.findParentByType(XmlTag::class.java)
                            ?: return@minBy 100

                    val myIndex = parentTags.indexOf(myTag)
                    return@minBy if (myIndex > -1) {
                        myIndex
                    } else {
                        100
                    }
                } ?: return@flatMap listOf<HtlVariableDeclaration>()

                listOf(closest)
            }
        }

    } else {
        applicableDeclarations
    }
    return result
}
