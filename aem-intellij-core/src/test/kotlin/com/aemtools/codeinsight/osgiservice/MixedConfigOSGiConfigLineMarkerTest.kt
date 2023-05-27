package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.navigationhandler.OSGiGutterIconNavigationHandler
import com.aemtools.common.constant.const
import com.aemtools.lang.java.JavaSearch
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiDsAnnotationsMixin
import com.aemtools.test.fixture.OSGiFelixAnnotationsMixin
import com.intellij.testFramework.VfsTestUtil

class MixedConfigOSGiConfigLineMarkerTest : BaseLightTest(),
    OSGiConfigFixtureMixin,
    OSGiFelixAnnotationsMixin,
    OSGiDsAnnotationsMixin {

  var tested: OSGiConfigLineMarker = OSGiConfigLineMarker()

  fun `test correct sorting of available configs with any extension`() = fileCase {
    addFelixServiceAnnotation()
    addClass("MyService.java", """
            package com.test;

            import ${const.java.FELIX_SERVICE_ANNOTATION};

            @Service
            public class MyService {}
        """)
    addEmptyOSGiConfigs(
        "/config/com.test.MyService-a.xml",
        "/config/com.test.MyService-b.xml",
        "/config.author/com.test.MyService-a.xml",
        "/config.author/com.test.MyService-b.cfg.json",
        "/config.author.dev/com.test.MyService-a.cfg.json",
        "/config.author.dev.perf/com.test.MyService-a.cfg.json",
        "/config.alongrunmodename/com.test.MyService-a.xml"
    )
    verify {
      val psiClass = JavaSearch.findClass("com.test.MyService", project)
          ?: throw AssertionError("Unable to find fixture class!")
      val classIdentifier = psiClass.nameIdentifier
          ?: throw AssertionError("Unable to get class identifier!")

      val marker = tested.getLineMarkerInfo(classIdentifier)
          ?: throw AssertionError("Marker is null")
      val navigationHandler = marker.navigationHandler as? OSGiGutterIconNavigationHandler
          ?: throw AssertionError("Navigation handler is null")

      val configs = navigationHandler.getSortedConfigs()

      assertEquals(listOf(
          "default a",
          "default b",
          "alongrunmodename a",
          "author a",
          "author b",
          "author, dev a",
          "author, dev, perf a"
      ), configs.map { "${it.mods.joinToString { it }} ${it.suffix()}" })
    }
  }

  fun `test updating of rendered configs if some config was removed`() = fileCase {
    addFelixServiceAnnotation()

    addClass("MyService.java", """
            package com.test;

            import ${const.java.FELIX_SERVICE_ANNOTATION};

            @Service
            public class MyService {}
        """)
    addEmptyOSGiConfigs(
        "/config/com.test.MyService.cfg.json",
        "/config.author/com.test.MyService.cfg.json",
        "/config.publish/com.test.MyService.xml",
        "/config.dev/com.test.MyService.xml"
    )
    verify {
      val psiClass = JavaSearch.findClass("com.test.MyService", project)
          ?: throw AssertionError("Unable to find fixture class!")
      val classIdentifier = psiClass.nameIdentifier
          ?: throw AssertionError("Unable to get class identifier!")

      val marker = tested.getLineMarkerInfo(classIdentifier)
          ?: throw AssertionError("Marker is null")
      val navigationHandler = marker.navigationHandler as? OSGiGutterIconNavigationHandler
          ?: throw AssertionError("Navigation handler is null")

      val configs = navigationHandler.getSortedConfigs()
      assertEquals(4, configs.size)

      val configVirtualFileToRemove = configs[0].file?.virtualFile
          ?: throw AssertionError("Couldn't get virtual file of osgi config")
      VfsTestUtil.deleteFile(configVirtualFileToRemove)

      val updatedConfigs = navigationHandler.getSortedConfigs()

      assertEquals(3, updatedConfigs.size)
      assertEquals(listOf("author", "dev", "publish"), updatedConfigs.map { it.mods.joinToString { it } })
    }
  }

  fun `test updating of rendered config list if new config was added`() = fileCase {
    addFelixServiceAnnotation()

    addClass("MyService.java", """
            package com.test;

            import ${const.java.FELIX_SERVICE_ANNOTATION};

            @Service
            public class MyService {}
        """)
    addEmptyOSGiConfigs(
        "/config/com.test.MyService-a.xml",
        "/config/com.test.MyService-b.xml",
        "/config.author/com.test.MyService-a.xml",
        "/config.author.dev/com.test.MyService-a.cfg.json",
        "/config.author.dev/com.test.MyService-b.cfg.json",
    )
    verify {
      val psiClass = JavaSearch.findClass("com.test.MyService", project)
          ?: throw AssertionError("Unable to find fixture class!")
      val classIdentifier = psiClass.nameIdentifier
          ?: throw AssertionError("Unable to get class identifier!")

      val marker = tested.getLineMarkerInfo(classIdentifier)
          ?: throw AssertionError("Marker is null")
      val navigationHandler = marker.navigationHandler as? OSGiGutterIconNavigationHandler
          ?: throw AssertionError("Navigation handler is null")

      val configs = navigationHandler.getSortedConfigs()
      assertEquals(5, configs.size)

      fixture.addFileToProject("/config.author/com.test.MyService-b.cfg.json", emptyJsonOSGiConfig())
      val updatedConfigs = navigationHandler.getSortedConfigs()

      assertEquals(6, updatedConfigs.size)
      assertEquals(listOf(
          "default a",
          "default b",
          "author a",
          "author b",
          "author, dev a",
          "author, dev b"
      ), updatedConfigs.map { "${it.mods.joinToString { it }} ${it.suffix()}" })
    }
  }
}
