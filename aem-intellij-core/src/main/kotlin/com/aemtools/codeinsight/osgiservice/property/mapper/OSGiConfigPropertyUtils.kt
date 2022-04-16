package com.aemtools.codeinsight.osgiservice.property.mapper

/**
 * @author Kostiantyn Diachenko
 */

/**
 * Maps OSGi R7 config method name to config property name.
 */
fun String.osgiConfigMethodToPropertyName(): String =
    this.replace("__", "_")
        .replace("_", ".")
        .replace("\$\\_\$", "-")
        .replace("\$\$", "\$")
        .replace("\$\$", "\$")
        .replace("\$", "")
