package com.aemtools.sync.settings.model

import com.aemtools.sync.util.SyncConstants

/**
 * Data class that represents AEM credentials.
 *
 * @property login the login
 * @property password the password
 *
 * @author Dmytro Liakhov
 */
data class AemCredentials(val login: String, val password: String) {

  companion object {

    /**
     * The default AEM credentials: __admin:admin__
     */
    val default: AemCredentials
      get() = AemCredentials(SyncConstants.DEFAULT_LOGIN, SyncConstants.DEFAULT_PASSWORD)

  }

}
