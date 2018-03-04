package com.aemtools.sync.settings.model

import com.aemtools.sync.util.SyncConstants

/**
 * Data class that represents single AEM instance.
 *
 * @property name the name of AEM instance (e.g. `author`)
 * @property urlAddress the address to the instance (e.g. `http://localhost:4502`)
 * @property group the group to which the instance belong (e.g. `qa`, `dev`)
 * @property credentials the credentials
 *
 * @author Dmytro Liakhov
 */
data class AemInstance(
    val name: String,
    val urlAddress: String,
    val group: String = "",
    val credentials: AemCredentials = AemCredentials.default
) {

  companion object {

    /**
     * The default author instance.
     */
    val default: AemInstance
      get() = AemInstance(
          SyncConstants.DEFAULT_INSTANCE_NAME,
          SyncConstants.DEFAULT_URL_INSTANCE,
          SyncConstants.DEFAULT_INSTANCE_GROUP
      )
  }

}
