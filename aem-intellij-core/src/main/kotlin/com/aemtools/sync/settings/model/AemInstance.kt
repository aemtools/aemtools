package com.aemtools.sync.settings.model

import com.aemtools.sync.util.SyncConstants

/**
 * @author Dmytro Liakhov
 */
data class AemInstance(
        val name: String,
        val urlAddress: String,
        val group: String = "",
        val credentials: AemCredentials = AemCredentials.default
) {

  companion object {
    val default: AemInstance
      get() = AemInstance(SyncConstants.DEFAULT_INSTANCE_NAME, SyncConstants.DEFAULT_URL_INSTANCE, SyncConstants.DEFAULT_INSTANCE_GROUP)
  }

}