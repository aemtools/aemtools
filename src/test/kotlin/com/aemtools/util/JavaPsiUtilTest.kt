package com.aemtools.util

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.fixture.OSGiConfigFixtureMixin
import com.aemtools.blocks.fixture.OSGiFelixAnnotationsMixin
import com.aemtools.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.constant.const.java.SLING_FILTER_ANNOTATION
import com.aemtools.constant.const.java.SLING_SERVLET_ANNOTATION
import com.aemtools.lang.java.JavaSearch

/**
 * @author Dmytro_Troynikov
 */
class JavaPsiUtilTest : BaseLightTest(),
    OSGiConfigFixtureMixin,
    OSGiFelixAnnotationsMixin {

  fun testIsOSGiServiceFelixAnnotated() = fileCase {
    addFelixServiceAnnotation()
    addClass("MyService.java", """
            package com.test;

            import $FELIX_SERVICE_ANNOTATION;

            @Service
            public class MyService {}
        """)
    verify {
      val psiClass = JavaSearch.findClass("com.test.MyService", project)
          ?: throw AssertionError("Unable to find fixture class!")

      assertTrue("The class should be determined as OSGi service", psiClass.isOSGiService())
    }
  }

  fun testSlingServletIsOSGiService() = fileCase {
    addFelixSlingServletAnnotation()
    addClass("MyServlet.java", """
            package com.test;

            import $SLING_SERVLET_ANNOTATION;

            @SlingServlet
            public class MyServlet {}
        """)
    verify {
      val psiClass = JavaSearch.findClass("com.test.MyServlet", project)
          ?: throw AssertionError("Unable to find fixture class!")

      assertTrue("The class annotated with SlingServlet annotation should be determined as OSGi service",
          psiClass.isOSGiService())
    }
  }

  fun testSlingFilterIsOSGiService() = fileCase {
    addFelixSlingFilterAnnotation()
    addClass("MyFilter.java", """
            package com.test;

            import $SLING_FILTER_ANNOTATION;

            @SlingFilter
            public class MyFilter {}
        """)
    verify {
      val psiClass = JavaSearch.findClass("com.test.MyFilter", project)
          ?: throw AssertionError("Unable to find fixture class!")

      assertTrue("The class annotated with SlingFilter annotation should be determined as OSGi service",
          psiClass.isOSGiService())
    }
  }

  // todo add test for declarative OSGi service declaration

}
