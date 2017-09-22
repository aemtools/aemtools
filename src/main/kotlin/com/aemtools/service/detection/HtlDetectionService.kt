package com.aemtools.service.detection

import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.constant.const.JCR_ROOT_SEPARATED
import com.aemtools.settings.HtlRootDirectories
import com.aemtools.util.OpenApiUtil.iAmTest
import com.intellij.openapi.project.Project

/**
 * The service responsible of Htl file type detection.
 *
 * @author Dmytro Troynikov
 */
object HtlDetectionService {

  @field:Volatile
  var markAllInTest: Boolean = true

  /**
   * Check if given file is Htl file.
   *
   * @param fileName the file name
   * @param project the project
   *
   * @return *true* if given file name is recognized as Htl,
   * *false* otherwise
   */
  fun isHtlFile(fileName: String, project: Project): Boolean {
    val extension = fileName.substringAfterLast(".")
    if (extension != "html") {
      return false
    }

    HtlRootDirectories.getInstance(project)?.let { roots ->
      if (roots.directories.any { fileName.startsWith(it) }) {
        return true
      }
    }

    return (markAllInTest && iAmTest()) || fileName.contains(JCR_ROOT_SEPARATED)
  }

  /**
   * Check if given directory is one of Htl roots.
   *
   * @param path directory to check
   * @param project the project
   *
   * @return *true* if given directory is Htl root, *false* otherwise
   */
  fun isHtlRootDirectory(path: String, project: Project): Boolean =
      HtlRootDirectories.getInstance(project)
          ?.directories?.contains(path) ?: false
          || path.substringAfterLast("/") == JCR_ROOT

  /**
   * Check if given path lays under some of Htl roots.
   *
   * @param path the path to check
   * @param project the project
   *
   * @return *true* if given path is part of Htl root,
   * *false* otherwise
   */
  fun isUnderHtlRoot(path: String, project: Project): Boolean {
    HtlRootDirectories.getInstance(project)?.let { roots ->
      if (roots.directories.any { it != path && path.startsWith(it) }) {
        return true
      }
    }

    return path.contains(JCR_ROOT_SEPARATED)
  }

  /**
   * Check if given directory may be marked as Htl root.
   *
   * @param path path to check
   * @param project the project
   *
   * @return *true* if given directory may be marked as htl root,
   * *false* otherwise
   */
  fun mayBeMarked(path: String, project: Project): Boolean {
    if (isJcrRoot(path)) {
      return false
    }

    return !isUnderHtlRoot(path, project)
  }

  private fun isJcrRoot(path: String): Boolean =
      path.substringAfterLast("/", "") == JCR_ROOT

}
