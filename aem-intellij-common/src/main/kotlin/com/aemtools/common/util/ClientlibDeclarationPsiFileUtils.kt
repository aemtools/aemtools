package com.aemtools.common.util

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile

/**
 * Get directory relative to current directory.
 *
 * @return relative directory, *null* if no such element exists
 */
fun PsiDirectory.myRelativeDirectory(path: String): PsiDirectory? {
  when {
    path.startsWith("../") ->
      return this.parentDirectory?.myRelativeDirectory(path.substringAfter("../"))
    path.contains("/") -> {
      val subdirName = path.split("/")[0]
      val subdir = this.findSubdirectory(subdirName)
          ?: return null
      val subPath = path.substring(path.indexOf("/") + 1)
      return subdir.myRelativeDirectory(subPath)
    }
    else -> return this
  }
}

/**
 * Get file relative to current directory.
 *
 * @return relative file, *null* if no such element exists
 */
fun PsiDirectory.myRelativeFile(path: String): PsiFile? {
  val normalizedPath = if (path.startsWith("./")) {
    path.substringAfter("./")
  } else {
    path
  }

  when {
    normalizedPath.startsWith("../") ->
      return this.parentDirectory?.myRelativeFile(path.substringAfter("../"))
    normalizedPath.contains("/") -> {
      val subdirName = normalizedPath.split("/")[0]
      val subdir = this.findSubdirectory(subdirName)
          ?: return null
      val subPath = normalizedPath.substring(normalizedPath.indexOf("/") + 1)
      return subdir.myRelativeFile(subPath)
    }
    else -> return this.findFile(normalizedPath)
  }
}
