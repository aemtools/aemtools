package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.navigationhandler.OSGiGutterIconNavigationHandler
import com.aemtools.common.constant.const.java.DS_COMPONENT_ANNOTATION
import com.aemtools.common.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.lang.java.JavaSearch
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiDsAnnotationsMixin
import com.aemtools.test.fixture.OSGiFelixAnnotationsMixin

/**
 * Test for [OSGiConfigLineMarker].
 *
 * @author Dmytro Primshyts
 */
class OSGiConfigLineMarkerTest : BaseLightTest(),
    OSGiConfigFixtureMixin,
    OSGiFelixAnnotationsMixin,
    OSGiDsAnnotationsMixin {

  var tested: OSGiConfigLineMarker = OSGiConfigLineMarker()

  fun testFelixServiceShouldBeMarked() = fileCase {
    addFelixServiceAnnotation()
    addClass("MyService.java", """
        package com.test;

        import $FELIX_SERVICE_ANNOTATION;

        @Service
        public class MyService {}
    """)
    addEmptyOSGiConfigs("/config/com.test.MyService.xml")

    verify {
      val psiClass = JavaSearch.findClass("com.test.MyService", project)
          ?: throw AssertionError("Unable to find fixture class!")
      val classIdentifier = psiClass.nameIdentifier
          ?: throw AssertionError("Unable to get class identifier!")

      val marker = tested.getLineMarkerInfo(classIdentifier)

      assertNotNull("Marker should be created for given identifier", marker)
    }
  }

  fun testDsComponentShouldBeMarked() = fileCase {
    addComponentAnnotation()
    addClass("MyService.java", """
        package com.test;

        import ${DS_COMPONENT_ANNOTATION};

        @Component
        public class MyService {}
    """)
    addEmptyOSGiConfigs("/config/com.test.MyService.xml")

    verify {
      val psiClass = JavaSearch.findClass("com.test.MyService", project)
          ?: throw AssertionError("Unable to find fixture class!")
      val classIdentifier = psiClass.nameIdentifier
          ?: throw AssertionError("Unable to get class identifier!")

      val marker = tested.getLineMarkerInfo(classIdentifier)

      assertNotNull("Marker should be created for given identifier", marker)
    }
  }

  fun testAvailableConfigsShouldBeSortedCorrectly() = fileCase {
    addFelixServiceAnnotation()
    addClass("MyService.java", """
            package com.test;

            import $FELIX_SERVICE_ANNOTATION;

            @Service
            public class MyService {}
        """)
    addEmptyOSGiConfigs(
        "/config/com.test.MyService-a.xml",
        "/config/com.test.MyService-b.xml",
        "/config.author/com.test.MyService-a.xml",
        "/config.author/com.test.MyService-b.xml",
        "/config.author.dev/com.test.MyService-a.xml",
        "/config.author.dev.perf/com.test.MyService-a.xml",
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

      val configs = navigationHandler.configs

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

}
