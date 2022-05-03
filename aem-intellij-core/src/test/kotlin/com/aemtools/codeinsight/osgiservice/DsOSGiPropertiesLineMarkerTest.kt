package com.aemtools.codeinsight.osgiservice

import com.aemtools.test.fixture.JavaMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiDsAnnotationsMixin
import org.assertj.core.groups.Tuple

/**
 * Tests for [DsOSGiPropertiesLineMarker].
 *
 * @author Kostiantyn Diachenko
 */
class DsOSGiPropertiesLineMarkerTest : BaseOSGiPropertyLineMarkerTest(),
    OSGiConfigFixtureMixin,
    OSGiDsAnnotationsMixin,
    JavaMixin {

  fun `test no marker info when no property attribute`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;

      @Component(
        name = "component name"
      )
      public class MyService {
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test no marker info when expression doesn't have =`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;

      @Component(
        property = "prop.name:Integer-10"
      )
      public class MyService {
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test no marker info when no osgi configs present`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;

      @Component(
        property = "prop.name:Integer=10"
      )
      public class MyService {
        $CARET
      }
    """)

    verify {
      hasNotOSGIPropertyLineMarker()
    }
  }

  fun `test marker info for single-value component property attribute as literal`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;

      @Component(
        property = "job.topics=/com/test/topic"
      )
      public class MyService {
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("\"job.topics=/com/test/topic\"", "OSGi Property")
      )
    }
  }

  fun `test marker info for single-value component property attribute as expression with constant`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;

      @Component(
        property = MyService.PROPERTY_NAME + "=/com/test/topic"
      )
      public class MyService {
        static final String PROPERTY_NAME = "property.name.key";
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("PROPERTY_NAME", "OSGi Property")
      )
    }
  }

  fun `test marker info for single-value component property attribute as expression with property type`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.apache.sling.commons.scheduler.Scheduler;

      @Component(
        property = Scheduler.PROPERTY_SCHEDULER_CONCURRENT + ":Boolean=false"
      )
      public class MyService {
          $CARET
      }
    """)

    addClass("Scheduler.java", """
      package org.apache.sling.commons.scheduler;

      public interface Scheduler {
        String PROPERTY_SCHEDULER_CONCURRENT = "scheduler.concurrent";
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("PROPERTY_SCHEDULER_CONCURRENT", "OSGi Property")
      )
    }
  }

  fun `test marker info for single-value component property attribute as expression with 2 constants`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import com.test.Constants;

      @Component(
        property = Constants.SERVICE_RANKING + "=" + Integer.MAX_VALUE
      )
      public class MyService {
          $CARET
      }
    """)

    addClass("Scheduler.java", """
      package com.test;

      public interface Constants {
        String SERVICE_RANKING = "service.ranking";
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("SERVICE_RANKING", "OSGi Property")
      )
    }
  }

  fun `test marker info for single-value component property attribute as expression at the end`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.apache.sling.commons.scheduler.Scheduler;

      @Component(
        property = "sling.servlet.paths=" + MyService.PATH
      )
      public class MyService {
        static final String PATH = "/services/servlet/test/path";
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("\"sling.servlet.paths=\"", "OSGi Property")
      )
    }
  }

  fun `test marker info for multi-value component property attribute as literal`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;

      @Component(
        property = { "job.topics=/com/test/topic" }
      )
      public class MyService {
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("\"job.topics=/com/test/topic\"", "OSGi Property")
      )
    }
  }

  fun `test marker info for multi-value component property attribute as expression with constant`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;

      @Component(
        property = { MyService.PROPERTY_NAME + "=/com/test/topic" }
      )
      public class MyService {
        static final String PROPERTY_NAME = "property.name.key";
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("PROPERTY_NAME", "OSGi Property")
      )
    }
  }

  fun `test marker info for multi-value component property attribute as expression with property type`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.apache.sling.commons.scheduler.Scheduler;

      @Component(
        property = { Scheduler.PROPERTY_SCHEDULER_CONCURRENT + ":Boolean=false" }
      )
      public class MyService {
          $CARET
      }
    """)

    addClass("Scheduler.java", """
      package org.apache.sling.commons.scheduler;

      public interface Scheduler {
        String PROPERTY_SCHEDULER_CONCURRENT = "scheduler.concurrent";
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("PROPERTY_SCHEDULER_CONCURRENT", "OSGi Property")
      )
    }
  }

  fun `test marker info for multi-value component property attribute as expression with 2 constants`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import com.test.Constants;

      @Component(
        property = { Constants.SERVICE_RANKING + "=" + Integer.MAX_VALUE }
      )
      public class MyService {
          $CARET
      }
    """)

    addClass("Scheduler.java", """
      package com.test;

      public interface Constants {
        String SERVICE_RANKING = "service.ranking";
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("SERVICE_RANKING", "OSGi Property")
      )
    }
  }

  fun `test marker info for multi-value component property attribute as expression at the end`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.apache.sling.commons.scheduler.Scheduler;

      @Component(
        property = { "sling.servlet.paths=" + MyService.PATH }
      )
      public class MyService {
        static final String PATH = "/services/servlet/test/path";
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("\"sling.servlet.paths=\"", "OSGi Property")
      )
    }
  }

  fun `test single marker info for line`() = fileCase {
    addComponentAnnotation()

    addClass("MyService.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;
      import org.apache.sling.commons.scheduler.Scheduler;

      @Component(
        property = "sling.servlet.paths=" + MyService.PATH
      )
      public class MyService {
        static final String PATH = "/services/servlet/test/path";
        $CARET
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    verify {
      hasOSGiPropertyLineMarker(
          getFixtureOsgiPropertyGutters(),
          lineMarkerElementTextAndTooltipExtractors,
          Tuple("\"sling.servlet.paths=\"", "OSGi Property")
      )
    }
  }
}
