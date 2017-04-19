package com.aemtools.index

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.index.search.OSGiConfigSearch

/**
 * @author Dmytro_Troynikov
 */
class OSGiConfigIndexTest : BaseLightTest() {

    fun testOSGiIndexMain() = fileCase {
        addXml("/config/com.test.Service.xml", """
            <jcr:root
                xmlns:sling="http://sling.apache.org/jcr/sling/1.0"
                xmlns:jcr="http://www.jcp.org/jcr/1.0"
                jcr:primaryType="sling:OsgiConfig"
          param1="value1"
          param2="value2"
          param3="value3"/>
        """)
        val osgiConfig = OSGiConfigSearch.findConfigsForClass("com.test.Service", project).firstOrNull()
                ?: throw AssertionError("Unable to find osgi config ")

        val value1 = osgiConfig.parameters["param1"]
        val value2 = osgiConfig.parameters["param2"]
        val value3 = osgiConfig.parameters["param3"]

        assertEquals("value1", value1)
        assertEquals("value2", value2)
        assertEquals("value3", value3)
    }

}