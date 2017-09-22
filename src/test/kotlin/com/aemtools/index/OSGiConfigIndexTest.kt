package com.aemtools.index

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.index.search.OSGiConfigSearch

/**
 * @author Dmytro_Troynikov
 */
class OSGiConfigIndexTest : BaseLightTest() {

  fun testOSGiIndexMain() = fileCase {
    addXml("config/com.test.Service.xml", """
            <jcr:root
                xmlns:sling="http://sling.apache.org/jcr/sling/1.0"
                xmlns:jcr="http://www.jcp.org/jcr/1.0"
                jcr:primaryType="sling:OsgiConfig"
          param1="value1"
          param2="value2"
          param3="value3"/>
        """)

    verify {
      val osgiConfig = OSGiConfigSearch.findConfigsForClass("com.test.Service", project).firstOrNull()
          ?: throw AssertionError("Unable to find osgi config ")

      assertEquals(mapOf(
          "param1" to "value1",
          "param2" to "value2",
          "param3" to "value3"
      ), osgiConfig.parameters)
    }
  }

  fun testOSGiIndexMain2() = fileCase {
    addXml("/$JCR_ROOT/configurations/config.author/com.test.config.Service.xml", """
            <jcr:root jcr:primaryType="sling:OsgiConfig"
                param1="value1"
                param2="value2"
                param3="value3"/>
        """)

    verify {
      val osgiConfig = OSGiConfigSearch.findConfigsForClass("com.test.config.Service", project).firstOrNull()
          ?: throw AssertionError("Unable to find osgi config")

      assertEquals(mapOf(
          "param1" to "value1",
          "param2" to "value2",
          "param3" to "value3"
      ), osgiConfig.parameters)

      assertEquals(listOf("author"), osgiConfig.mods)
      assertEquals("com.test.config.Service.xml", osgiConfig.fileName)
      assertEquals("com.test.config.Service", osgiConfig.fullQualifiedName)
    }
  }

}
