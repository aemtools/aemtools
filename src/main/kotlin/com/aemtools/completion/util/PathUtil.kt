package com.aemtools.completion.util

import com.aemtools.constant.const.JCR_ROOT_SEPARATED

/**
 * @author Dmytro Troynikov
 */

/**
 * Normalize given path to *jcr_root* folder.
 * e.g.:
 *
 * ```
 *  .../src/jcr_root/apps/components -> /apps/components
 * ```
 * @receiver [String]
 * @return path normalized to jcr_root
 */
fun String.normalizeToJcrRoot(): String =
    "/${substringAfter(JCR_ROOT_SEPARATED)}"

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
 *
 * @receiver [String]
 * @return new relative path
 */
fun String.relativeTo(path: String): String =
    if (startsWith("$path/")) {
      substring(path.length + 1)
    } else {
      this
    }
