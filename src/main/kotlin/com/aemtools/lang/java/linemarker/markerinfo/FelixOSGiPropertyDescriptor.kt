package com.aemtools.lang.java.linemarker.markerinfo

import com.intellij.psi.xml.XmlAttribute

/**
 * Descriptor of single OSGi property.
 *
 * @author Dmytro Troynikov
 */
data class FelixOSGiPropertyDescriptor(
        /**
         * Mods string.
         */
        val mods: String,
        /**
         * Value of property.
         */
        val propertyValue: String,
        /**
         * Xml Attribute - declaration of property.
         */
        val xmlAttribute: XmlAttribute
)