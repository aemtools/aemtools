package com.aemtools.index

import com.aemtools.common.constant.const
import com.aemtools.index.search.OSGiConfigSearch
import com.aemtools.test.base.BaseLightTest

class JsonOSGiConfigIndexTest : BaseLightTest() {

  fun testOSGiIndexMain() = fileCase {
    addJson("config/com.test.Service.cfg.json", """
        {
        "param1":"value1",
        "param2":"value2",
        "param3":"value3"
        }
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
    addJson("/${const.JCR_ROOT}/configurations/config.author/com.test.config.Service.cfg.json", """
        {
        "param1":"value1",
        "param2":"value2",
        "param3":"value3"
        }
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
      assertEquals("com.test.config.Service.cfg.json", osgiConfig.fileName)
      assertEquals("com.test.config.Service", osgiConfig.fullQualifiedName)
    }
  }

  fun testOSGiConfigPropertyMapper() = fileCase {
    addJson("/${const.JCR_ROOT}/configurations/config.author/com.test.config.Service.cfg.json", """
        {
        "param1":"value1",
        "param2": true,
        "param3": 1,
        "param4": 1.1,
        "param5": [],
        "param6": ["1", "2"],
        "param7": [true, false],
        "param8": [1, 2],
        "param9": [1.1, 2.2],
        "param10": ["1", 2],
        "param11": null,
        "param12": {
          "param12.1": true
        }
        }
        """)

    verify {
      val osgiConfig = OSGiConfigSearch.findConfigsForClass("com.test.config.Service", project).firstOrNull()
          ?: throw AssertionError("Unable to find osgi config")

      assertEquals(mapOf(
          "param1" to "value1",
          "param2" to "true",
          "param3" to "1.0",
          "param4" to "1.1",
          "param5" to "",
          "param6" to "1,2",
          "param7" to "true,false",
          "param8" to "1.0,2.0",
          "param9" to "1.1,2.2",
          "param10" to "1,2.0",
          "param11" to null,
          "param12" to null
      ), osgiConfig.parameters)

      assertEquals(listOf("author"), osgiConfig.mods)
      assertEquals("com.test.config.Service.cfg.json", osgiConfig.fileName)
      assertEquals("com.test.config.Service", osgiConfig.fullQualifiedName)
    }
  }

  fun testOSGiIndexDotInName() = fileCase {
    addJson("/${const.JCR_ROOT}/configurations/config.author/com.test.config.Service.factory-one.cfg.json", """
        {}
    """)

    verify {
      val osgiConfig = requireNotNull(
          OSGiConfigSearch.findConfigsForClass("com.test.config.Service", project).firstOrNull()
      )

      assertEquals("com.test.config.Service.factory-one.cfg.json", osgiConfig.fileName)
      assertEquals("com.test.config.Service", osgiConfig.fullQualifiedName)
    }
  }

}
