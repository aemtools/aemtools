package com.aemtools.completion.util

import com.aemtools.constant.const
import com.aemtools.lang.htl.psi.*
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.psi.xml.XmlAttribute

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
 */
fun HtlVariableName.isOption() : Boolean {
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
 * Check if current [HtlHtlEl] element resides isWithin attribute with given name.
 * @param attributeName the name of attribute
 * @return __true__ if current element is the value of attribute with given name
 */
fun HtlHtlEl.isInsideOF(attributeName: String) : Boolean {
    val html = this.containingFile.getHtmlFile()
            ?: return false
    val attribute = html.findElementAt(this.textOffset - 1)
            .findParentByType(XmlAttribute::class.java) ?: return false

    return attribute.name.startsWith(attributeName)
}