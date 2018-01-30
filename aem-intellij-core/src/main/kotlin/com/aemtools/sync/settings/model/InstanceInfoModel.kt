package com.aemtools.sync.settings.model

import java.io.Serializable

data class InstanceInfoModel(val enabled: Boolean?, val url: String?,
                             val login: String?, val password: String?) : Serializable
