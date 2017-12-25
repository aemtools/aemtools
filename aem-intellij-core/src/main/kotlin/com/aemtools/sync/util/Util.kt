package com.aemtools.sync.util

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.common.constant.const.JCR_ROOT_SEPARATED
import com.intellij.openapi.vfs.VirtualFile

/**
 * @author Dmytro Liakhov
 */

fun VirtualFile.isUnderAEMRoot() : Boolean =
        this.path.contains(JCR_ROOT_SEPARATED)

fun VirtualFile.getPathOnAEMInstance() : String =
        this.path.substringAfter("/$JCR_ROOT")