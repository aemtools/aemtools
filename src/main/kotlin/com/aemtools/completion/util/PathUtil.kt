package com.aemtools.completion.util

import com.aemtools.constant.const.JCR_ROOT_SEPARATED

/**
 * @author Dmytro Troynikov
 */
fun String.normalizeToJcrRoot(): String =
    substring(indexOf(JCR_ROOT_SEPARATED) + JCR_ROOT_SEPARATED.length - 1)
