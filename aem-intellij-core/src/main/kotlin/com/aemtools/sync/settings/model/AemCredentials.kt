package com.aemtools.sync.settings.model

import com.aemtools.sync.util.SyncConstants

/**
 * @author Dmytro Liakhov
 */
data class AemCredentials(val login: String, val password: String) {

  companion object {

    val default: AemCredentials
      get() = AemCredentials(SyncConstants.DEFAULT_LOGIN, SyncConstants.DEFAULT_PASSWORD)

  }

}