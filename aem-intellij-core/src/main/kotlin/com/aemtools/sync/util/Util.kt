package com.aemtools.sync.util

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.common.constant.const.JCR_ROOT_SEPARATED
import com.intellij.openapi.vfs.VirtualFile

/**
 * @author Dmytro Liakhov
 */

/**
 * Check if current [VirtualFile] is under JCR root.
 *
 * @receiver [VirtualFile]
 * @return *true* if current file is under JCR root,
 * *false* otherwise
 */
fun VirtualFile.isUnderJcrRoot(): Boolean =
    this.path.contains(JCR_ROOT_SEPARATED)

/**
 * Extract JCR path for current virtual file.
 *
 * @receiver [VirtualFile]
 * @return JCR path of current file
 */
fun VirtualFile.getJcrPath(): String =
    this.path.substringAfter("/$JCR_ROOT")

/**
 * Extract root of current virtual file.
 *
 * @receiver [VirtualFile]
 * @return root path of current file
 */
fun VirtualFile.getRootOfFile(): String =
    this.path.substringBefore("/$JCR_ROOT")
