package com.aemtools.integration.sync.util

import com.intellij.openapi.application.PathManager

/**
 * @author Dmytro Liakhov
 */
object SyncConstants {

  const val TMP_FILE_PACKAGE_NAME = "package.zip"
  const val TMP_NAME = "name"
  const val TMP_GROUP = "group"

  val TMP_FILE_NAME_DOWNLOAD_PACKAGE = "${PathManager.getTempPath()}/$TMP_FILE_PACKAGE_NAME"

  const val SETTINGS_ID = "preference.AEMTools"

  const val DISPLAY_NAME_SETTINGS = "AEM Tools"

  const val DEFAULT_INSTANCE_NAME = "author"
  const val DEFAULT_INSTANCE_GROUP = "local"
}
