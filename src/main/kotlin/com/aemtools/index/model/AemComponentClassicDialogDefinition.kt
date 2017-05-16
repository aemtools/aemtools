package com.aemtools.index.model

import java.io.Serializable

/**
 * @author Dmytro Troynikov
 */
data class AemComponentClassicDialogDefinition(val path: String)
    : Serializable {

    companion object {
        @JvmStatic
        val serialVersionUID: Long = 1L
    }

}