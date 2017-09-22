package com.aemtools.index.search

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.fixture.OSGiConfigFixtureMixin
import com.aemtools.index.model.sortByMods

/**
 * @author Dmytro Troynikov
 */
class OSGiConfigSearchTest : BaseLightTest(),
    OSGiConfigFixtureMixin {

  fun testBasicSearch() = fileCase {
    addEmptyOSGiConfigs("/config/com.test.Service.xml")

    verify {
      OSGiConfigSearch.findConfigsForClass("com.test.Service", project)
          .firstOrNull()
          ?: throw AssertionError("Unable to find configuration")
    }
  }

  fun testSearchForOSGiServiceFactory() = fileCase {
    addEmptyOSGiConfigs(
        "/config/com.test.Service-first.xml",
        "/config/com.test.Service-second.xml"
    )

    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project)

      assertEquals(2, configs.size)
    }
  }

  fun testSearchForOSGiConfigurationFactory() = fileCase {
    addEmptyOSGiConfigs(
        "/config/com.test.Service-my-long-name.xml",
        "/config/com.test.Service-my-very-long-name-2.xml"
    )

    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project)

      assertEquals(2, configs.size)
      assertEquals(listOf(
          "my-long-name",
          "my-very-long-name-2"
      ),
          configs.sortByMods().map { it.suffix() }
      )
    }
  }

  fun testBasicSearchForOSGiServiceWithFile() = fileCase {
    val filesNames = listOf(
        "/config/com.test.Service.xml",
        "/config.author/com.test.Service.xml"
    )
    addEmptyOSGiConfigs(*filesNames.toTypedArray())

    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project, true)

      assertEquals(
          filesNames.map { "/src$it" },
          configs.sortByMods().map { it.xmlFile?.virtualFile?.path }
      )
    }
  }

  fun testSearchForOSGiServiceFactoryConfigs() = fileCase {
    val fileNames = listOf(
        "/config/com.test.Service-first.xml",
        "/config/com.test.Service-second.xml"
    )
    addEmptyOSGiConfigs(*fileNames.toTypedArray())
    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project, true)
      assertEquals(2, configs.size)
      assertEquals(
          fileNames.map { "/src$it" },
          configs.sortByMods().map { it.xmlFile?.virtualFile?.path }
      )
    }
  }
}
