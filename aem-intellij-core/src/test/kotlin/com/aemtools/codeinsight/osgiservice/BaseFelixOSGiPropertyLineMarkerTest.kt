package com.aemtools.codeinsight.osgiservice

import com.aemtools.test.fixture.JavaMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiFelixAnnotationsMixin
import org.assertj.core.groups.Tuple

/**
 * Test for [FelixOSGiPropertyLineMarker].
 *
 * @author Dmytro Primshyts
 */
abstract class BaseFelixOSGiPropertyLineMarkerTest(private val osgiConfigExtension: String)
  : BaseOSGiPropertyLineMarkerTest(),
    OSGiConfigFixtureMixin,
    OSGiFelixAnnotationsMixin,
    JavaMixin {

  fun `test marker info for Felix method property declaration`() = fileCase {
    addFelixServiceAnnotation()
    addFelixPropertyAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Service;
      import org.apache.felix.scr.annotations.Property;

      @Service
      public class MyService {

        @Property
        private static final String ${CARET}TEST = "test.property";

      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService$osgiConfigExtension"
    )

    osgiConfig("/config/author/com.test.MyService.xml", mapOf("test.property" to "test value"))

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("TEST", "OSGi Property")
      )
    }
  }

  fun `test no marker info for Felix method property declaration without existed configs`() = fileCase {
    addFelixServiceAnnotation()
    addFelixPropertyAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Service;
      import org.apache.felix.scr.annotations.Property;

      @Service
      public class MyService {

        @Property
        private static final String ${CARET}TEST = "test.property";

      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test marker info for Felix class level property with literal name`() = fileCase {
    addFelixServiceAnnotation()
    addFelixPropertyAnnotation()
    addFelixPropertiesAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Service;
      import org.apache.felix.scr.annotations.Property;
      import org.apache.felix.scr.annotations.Properties;

      @Service
      @Properties({
        @Property(name = "test.property", value = "testValue")
      })
      public class MyService {
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService$osgiConfigExtension"
    )

    osgiConfig("/config/author/com.test.MyService$osgiConfigExtension", mapOf("test.property" to "test value"))

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          listOf(
              java.util.function.Function { it.element?.parent?.text },
              java.util.function.Function { it.lineMarkerTooltip }
          ),
          Tuple("name = \"test.property\"", "OSGi Property")
      )
    }
  }

  fun `test marker info for Felix class level property with name from current class constants`() = fileCase {
    addFelixServiceAnnotation()
    addFelixPropertyAnnotation()
    addFelixPropertiesAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Service;
      import org.apache.felix.scr.annotations.Property;
      import org.apache.felix.scr.annotations.Properties;

      @Service
      @Properties({
        @Property(name = MyService.PROPERTY_KEY, value = "testValue")
      })
      public class MyService { 
        static final String PROPERTY_KEY = "test.property";
        $CARET
      }
    """)

    osgiConfig("/config/author/com.test.MyService$osgiConfigExtension", mapOf("test.property" to "test value"))

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          listOf(
              java.util.function.Function { it.element?.parent?.text },
              java.util.function.Function { it.lineMarkerTooltip }
          ),
          Tuple("name = MyService.PROPERTY_KEY", "OSGi Property")
      )
    }
  }

  fun `test no marker info for Felix class level private property`() = fileCase {
    addFelixServiceAnnotation()
    addFelixPropertyAnnotation()
    addFelixPropertiesAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Service;
      import org.apache.felix.scr.annotations.Property;
      import org.apache.felix.scr.annotations.Properties;

      @Service
      @Properties({
        @Property(name = MyService.PROPERTY_KEY, value = "testValue", propertyPrivate = true)
      })
      public class MyService { 
        static final String PROPERTY_KEY = "test.property";
        $CARET
      }
    """)

    addEmptyOSGiConfigs("/config/author/com.test.MyService$osgiConfigExtension")

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test marker info for Felix class level property with name from external class constants`() = fileCase {
    addFelixServiceAnnotation()
    addFelixPropertyAnnotation()
    addFelixPropertiesAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Service;
      import org.apache.felix.scr.annotations.Property;
      import org.apache.felix.scr.annotations.Properties;
      import static com.test.Constants.ANOTHER_PROPERTY_NAME;

      @Service
      @Properties({
        @Property(name = ANOTHER_PROPERTY_NAME, value = "testValue")
      })
      public class MyService {
        $CARET
      }
    """)

    addClass("Constants.java", """
      package com.test;

      public final class Constants { 
        public static final String ANOTHER_PROPERTY_NAME = "another.test.property";
      }
    """)

    osgiConfig("/config/author/com.test.MyService$osgiConfigExtension", mapOf("test.property" to "test value"))

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          listOf(
              java.util.function.Function { it.element?.parent?.text },
              java.util.function.Function { it.lineMarkerTooltip }
          ),
          Tuple("name = ANOTHER_PROPERTY_NAME", "OSGi Property")
      )
    }
  }

  fun `test no marker info for Felix class level property without property declaration`() = fileCase {
    addFelixServiceAnnotation()
    addFelixPropertyAnnotation()
    addFelixPropertiesAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Service;
      import org.apache.felix.scr.annotations.Property;
      import org.apache.felix.scr.annotations.Properties;

      @Service
      @Component(name = "Service name")
      public class MyService {
        $CARET
      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

}
