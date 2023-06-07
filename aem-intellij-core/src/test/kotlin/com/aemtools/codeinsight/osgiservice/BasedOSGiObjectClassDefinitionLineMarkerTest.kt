package com.aemtools.codeinsight.osgiservice

import com.aemtools.test.fixture.JavaMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiDsAnnotationsMixin
import org.assertj.core.groups.Tuple

/**
 * Test for [OSGiObjectClassDefinitionLineMarker].
 *
 * @author Kostiantyn Diachenko
 */
abstract class BasedOSGiObjectClassDefinitionLineMarkerTest(private val osgiConfigExtension: String)
  : BaseOSGiPropertyLineMarkerTest(),
    OSGiConfigFixtureMixin,
    OSGiDsAnnotationsMixin,
    JavaMixin {

  fun `test marker info DS OSGi Config with OCD in separate class`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("Config.java", """
      package com.test;

      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      
      @ObjectClassDefinition(name = "Configuration")
      public @interface Config {

        @AttributeDefinition(
            name = "Test property",
            description = "Test property description")
        String ${CARET}test1Property();

      }
    """)

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.Designate;
      import com.test.Config;

      @Component
      @Designate(ocd = Config.class)
      public class MyService {

      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService$osgiConfigExtension"
    )

    osgiConfig("/config/author/com.test.MyService$osgiConfigExtension", mapOf("test1Property" to "test value"))

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("test1Property", "OSGi Property")
      )
    }
  }

  fun `test marker info DS OSGi Config with OCD as inner class`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      import org.osgi.service.metatype.annotations.Designate;

      @Component
      @Designate(ocd = MyService.Config.class)
      public class MyService {
      
        @ObjectClassDefinition(name = "Configuration")
        public @interface Config {
  
          @AttributeDefinition(
              name = "Test property",
              description = "Test property description")
          String ${CARET}test2Property();
        }
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService$osgiConfigExtension"
    )

    osgiConfig("/config/author/com.test.MyService$osgiConfigExtension", mapOf("test2Property" to "test value"))

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("test2Property", "OSGi Property")
      )
    }
  }

  /**
   * Note: Class file doesn't provide PsiJavaCodeReferenceElement
   */
  fun `test marker info DS OSGi Config when OCD as inner annotation in _class file`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("MyService.class", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      import org.osgi.service.metatype.annotations.Designate;

      @Component
      @Designate(ocd = Config.class)
      public class MyService {
      
        @ObjectClassDefinition(name = "Configuration")
        public @interface Config {
  
          @AttributeDefinition(
              name = "Test property",
              description = "Test property description")
          String ${CARET}test1_property();
        }
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService$osgiConfigExtension"
    )

    osgiConfig("/config/author/com.test.MyService$osgiConfigExtension", mapOf("test1.property" to "test value"))

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("test1_property", "OSGi Property")
      )
    }
  }

  fun `test no marker info DS OSGi Config when OCD in not marked as OCD`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("MyService.class", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.Designate;

      @Component
      @Designate(ocd = Config.class)
      public class MyService {
          
        public @interface Config {
  
          @AttributeDefinition(
              name = "Test property",
              description = "Test property description")
          String ${CARET}test_property();
        }
      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test no marker info DS OSGi Config when OCD method doesn't have AttributeDefinition annotation`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      import org.osgi.service.metatype.annotations.Designate;

      @Component
      @Designate(ocd = Config.class)
      public class MyService { 
        
        @ObjectClassDefinition(name = "Configuration")
        public @interface Config {
          String ${CARET}test_property();
        }
      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test no marker info DS OSGi Config when no config files for current OCD`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("MyService.class", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      import org.osgi.service.metatype.annotations.Designate;

      @Component
      @Designate(ocd = Config.class)
      public class MyService { 
        
        @ObjectClassDefinition(name = "Configuration")
        public @interface Config {
          @AttributeDefinition(
              name = "Test property",
              description = "Test property description")
          String ${CARET}test_property();
        }
      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test no marker info DS OSGi Config when component doesn't have Designate annotation`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;

      @Component(ocd = Config.class)
      public class MyService { 
        
        @ObjectClassDefinition(name = "Configuration")
        public @interface Config {
          @AttributeDefinition(
              name = "Test property",
              description = "Test property description")
          String ${CARET}test_property();
        }
      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test no marker info DS OSGi Config when it is referenced not via Designate annotation`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("MyService.class", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      import org.osgi.service.metatype.annotations.AttributeDefinition;

      @Component(ocd = Config.class)
      public class MyService { 
        
        @ObjectClassDefinition(name = "Configuration")
        public @interface Config {
          @AttributeDefinition(
              name = "Test property",
              description = "Test property description")
          String ${CARET}test_property();
        }
      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test no marker info DS OSGi Config when component is referenced to another OCD`() = fileCase {
    addBasicOSGiDeclarativeServiceAnnotations()

    addClass("Config.java", """
      package com.test;

      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      
      @ObjectClassDefinition(name = "Other Configuration")
      public @interface OtherConfig {
        @AttributeDefinition(
            name = "Test property",
            description = "Test property description")
        String ${CARET}testProperty();
      }
    """)

    addClass("MyService.class", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.osgi.service.metatype.annotations.AttributeDefinition;
      import org.osgi.service.metatype.annotations.ObjectClassDefinition;
      import org.osgi.service.metatype.annotations.Designate;

      @Component
      @Designate(ocd = OtherConfig.class)
      public class MyService { 
        
        @ObjectClassDefinition(name = "Configuration")
        public @interface Config {
          @AttributeDefinition(
              name = "Test property",
              description = "Test property description")
          String ${CARET}test_property();
        }
      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }
}
