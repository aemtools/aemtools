package com.aemtools.completion.util

import com.aemtools.constant.const

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