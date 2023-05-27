package com.aemtools.index.search

import com.aemtools.index.model.sortByMods
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.OSGiConfigFixtureMixin

/**
 * @author Dmytro Primshyts
 */
class OSGiConfigSearchTest : BaseLightTest(),
    OSGiConfigFixtureMixin {

  fun testBasicSearchForXmlConfig() = fileCase {
    addEmptyOSGiConfigs("/config/com.test.Service.xml")

    verify {
      OSGiConfigSearch.findConfigsForClass("com.test.Service", project)
          .firstOrNull()
          ?: throw AssertionError("Unable to find configuration")
    }
  }

  fun testBasicSearchForJsonConfig() = fileCase {
    addEmptyOSGiConfigs("/config/com.test.Service.cfg.json")

    verify {
      OSGiConfigSearch.findConfigsForClass("com.test.Service", project)
          .firstOrNull()
          ?: throw AssertionError("Unable to find configuration")
    }
  }

  fun testSearchForXmlOSGiServiceFactory() = fileCase {
    addEmptyOSGiConfigs(
        "/config/com.test.Service-first.xml",
        "/config/com.test.Service-second.xml"
    )

    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project)

      assertEquals(2, configs.size)
    }
  }

  fun testSearchForJsonOSGiServiceFactory() = fileCase {
    addEmptyOSGiConfigs(
        "/config/com.test.Service-first.cfg.json",
        "/config/com.test.Service-second.cfg.json"
    )

    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project)

      assertEquals(2, configs.size)
    }
  }

  fun testSearchForXmlOSGiConfigurationFactory() = fileCase {
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

  fun testSearchForJsonOSGiConfigurationFactory() = fileCase {
    addEmptyOSGiConfigs(
        "/config/com.test.Service-my-long-name.cfg.json",
        "/config/com.test.Service-my-very-long-name-2.cfg.json"
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

  fun testSearchForMixedOSGiConfigurationFactory() = fileCase {
    addEmptyOSGiConfigs(
        "/config/com.test.Service-my-long-name.xml",
        "/config/com.test.Service-my-very-long-name-2.cfg.json"
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

  fun testBasicSearchForXmlOSGiServiceWithFile() = fileCase {
    val filesNames = listOf(
        "/config/com.test.Service.xml",
        "/config.author/com.test.Service.xml"
    )
    addEmptyOSGiConfigs(*filesNames.toTypedArray())

    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project, true)

      assertEquals(
          filesNames.map { "/src$it" },
          configs.sortByMods().map { it.file?.virtualFile?.path }
      )
    }
  }

  fun testBasicSearchForJsonOSGiServiceWithFile() = fileCase {
    val filesNames = listOf(
        "/config/com.test.Service.cfg.json",
        "/config.author/com.test.Service.cfg.json"
    )
    addEmptyOSGiConfigs(*filesNames.toTypedArray())

    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project, true)

      assertEquals(
          filesNames.map { "/src$it" },
          configs.sortByMods().map { it.file?.virtualFile?.path }
      )
    }
  }

  fun testBasicSearchForMixedOSGiServiceWithFile() = fileCase {
    val filesNames = listOf(
        "/config/com.test.Service.xml",
        "/config.author/com.test.Service.cfg.json"
    )
    addEmptyOSGiConfigs(*filesNames.toTypedArray())

    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project, true)

      assertEquals(
          filesNames.map { "/src$it" },
          configs.sortByMods().map { it.file?.virtualFile?.path }
      )
    }
  }

  fun testSearchForXmlOSGiServiceFactoryConfigs() = fileCase {
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
          configs.sortByMods().map { it.file?.virtualFile?.path }
      )
    }
  }

  fun testSearchForJsonOSGiServiceFactoryConfigs() = fileCase {
    val fileNames = listOf(
        "/config/com.test.Service-first.cfg.json",
        "/config/com.test.Service-second.cfg.json"
    )
    addEmptyOSGiConfigs(*fileNames.toTypedArray())
    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project, true)
      assertEquals(2, configs.size)
      assertEquals(
          fileNames.map { "/src$it" },
          configs.sortByMods().map { it.file?.virtualFile?.path }
      )
    }
  }

  fun testSearchForMixedOSGiServiceFactoryConfigs() = fileCase {
    val fileNames = listOf(
        "/config/com.test.Service-first.xml",
        "/config/com.test.Service-second.cfg.json"
    )
    addEmptyOSGiConfigs(*fileNames.toTypedArray())
    verify {
      val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project, true)
      assertEquals(2, configs.size)
      assertEquals(
          fileNames.map { "/src$it" },
          configs.sortByMods().map { it.file?.virtualFile?.path }
      )
    }
  }
}
