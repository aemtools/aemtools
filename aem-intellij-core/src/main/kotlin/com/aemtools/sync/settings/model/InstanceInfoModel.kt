package com.aemtools.sync.settings.model

/**
 * @author Dmytro Liakhov
 */
data class InstanceInfoModel(val enabled: Boolean,
                             val url: String,
                             val login: String,
                             val password: String)
