package com.aemtools.lang.util

import com.aemtools.common.util.virtualFile
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.xml.XmlTag

fun XmlTag.isFileWithCqNamespace(): Boolean = virtualFile()?.isFileWithCqNamespace() ?: false

fun VirtualFile.isFileWithCqNamespace(): Boolean =
    path.matches(Regex(".*/_cq_[^/]+(/\\.content)?\\.xml"))
