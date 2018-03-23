package com.aemtools.common.util

import com.intellij.openapi.vfs.VirtualFile

/**
 * Get resource type from current [VirtualFile].
 *
 * @receiver [VirtualFile]
 *
 * @return resource type of current virtual file
 */
fun VirtualFile.resourceType(): String? {
  var currentDir: VirtualFile? = this
  while (currentDir != null) {
    if (currentDir.findChild(".content.xml") != null) {
      return currentDir.path.normalizeToJcrRoot()
    }
    currentDir = currentDir.parent
  }
  return null
}
