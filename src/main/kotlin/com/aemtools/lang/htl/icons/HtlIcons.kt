package com.aemtools.lang.htl.icons

import com.intellij.openapi.util.IconLoader

/**
 * @author Dmytro Troynikov
 */
object HtlIcons {

    val HTL_FILE = icon("htl.png")

    private fun icon(name :String) = IconLoader.getIcon("/icons/$name", HtlIcons::class.java)

}