package com.aemtools.completion.util

import com.aemtools.constant.const.JCR_ROOT_SEPARATED

/**
 * @author Dmytro Troynikov
 */
fun String.normalizeToJcrRoot(): String =
    substring(indexOf(JCR_ROOT_SEPARATED) + JCR_ROOT_SEPARATED.length - 1)

/**
 * Given:
 * Current string is a path to file, input string is path to file
 * within current directory.
 *
 * ```
 * this = /some/path
 * path = /some/path/under/file.html
 * result = under/file.html
 * ```
 */
fun String.relativeTo(path: String) : String =
        if (startsWith(path)) {
            substring(path.length + 1)
        } else {
            this
        }