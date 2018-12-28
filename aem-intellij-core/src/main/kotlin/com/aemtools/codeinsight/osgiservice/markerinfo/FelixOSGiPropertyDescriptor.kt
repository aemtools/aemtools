package com.aemtools.codeinsight.osgiservice.markerinfo

import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile

/**
 * Descriptor of single OSGi property.
 *
 * @author Dmytro Primshyts
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
        val xmlAttribute: XmlAttribute?,

        /**
         * OSGi configuration XML file.
         */
        val osgiConfigFIle: XmlFile
)
