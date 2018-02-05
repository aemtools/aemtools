package com.aemtools.sync.util

import com.intellij.openapi.application.PathManager

/**
 * @author Dmytro Liakhov
 */
object SyncConstants {

  val TMP_FILE_PACKAGE_NAME = "package.zip"
  val TMP_NAME = "name"
  val TMP_GROUP = "group"

  val TMP_FILE_NAME_DOWNLOAD_PACKAGE = "${PathManager.getTempPath()}/${SyncConstants.TMP_FILE_PACKAGE_NAME}"

  val SETTINGS_ID = "preference.AEMTools"

  val DISPLAY_NAME_SETTINGS = "AEM Tools"

  val DEFAULT_LOGIN = "admin"
  val DEFAULT_PASSWORD = "admin"
  val DEFAULT_URL_INSTANCE = "http://localhost:4502"
  val DEFAULT_IS_ENABLED_SYNC = false

}
