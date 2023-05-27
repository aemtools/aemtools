package com.aemtools.codeinsight.osgiservice.property.provider

import com.aemtools.codeinsight.osgiservice.OSGiObjectClassDefinitionLineMarker
import com.aemtools.codeinsight.osgiservice.navigationhandler.OSGiPropertyNavigationHandler
import com.aemtools.lang.java.JavaSearch
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JavaMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiDsAnnotationsMixin

abstract class BaseOSGiPropertyDescriptorsProviderTest(private val osgiConfigExtension: String) : BaseLightTest(),
    OSGiConfigFixtureMixin,
    OSGiDsAnnotationsMixin,
    JavaMixin {

  fun testOsgiComponentConfigPropertyMarkerInfo() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("Config.java", """
      package com.test;

      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      
      @ObjectClassDefinition(name = "Configuration")
      public @interface Config {

        @AttributeDefinition(
            name = "Test property",
            description = "Test property description")
        String ${CARET}testProperty();

      }
    """)

    addClass("MyService.java", """
      package com.test;

      import com.test.Config;
      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.Designate;

      @Component
      @Designate(ocd = Config.class)
      public class MyService {

      }
    """)

    addEmptyOSGiConfigs("/config/com.test.MyService$osgiConfigExtension")
    osgiConfig("/config/author/com.test.MyService$osgiConfigExtension", mapOf("testProperty" to "a"))
    osgiConfig("/config/publish/com.test.MyService$osgiConfigExtension", mapOf("testProperty" to "b"))
    osgiConfig("/config/publish.prod/com.test.MyService$osgiConfigExtension", mapOf("testProperty" to "c"))

    verify {
      val osgiOcdPsiClass = JavaSearch.findClass("com.test.Config", project)
          ?: throw AssertionError("Unable to find fixture class!")

      val methodIdentifier = osgiOcdPsiClass.allMethods.first().nameIdentifier
          ?: throw AssertionError("Unable to get OCD method!")

      val marker = OSGiObjectClassDefinitionLineMarker().getLineMarkerInfo(methodIdentifier)
          ?: throw AssertionError("Marker is null")
      val navigationHandler = marker.navigationHandler as? OSGiPropertyNavigationHandler
          ?: throw AssertionError("Navigation handler is null")

      val configs = navigationHandler.propertyDescriptors()

      assertEquals(listOf(
          "default       <no value set>",
          "author        a",
          "publish       b",
          "publish, prod c"
      ), configs.map { "${it.mods} ${it.propertyValue}" })
    }
  }
}

