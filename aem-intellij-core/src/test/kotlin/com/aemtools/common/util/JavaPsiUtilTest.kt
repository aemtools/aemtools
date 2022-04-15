package com.aemtools.common.util

import com.aemtools.common.constant.const.java.FELIX_PROPERTY_ANNOTATION
import com.aemtools.common.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_FILTER_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_HEALTH_CHECK_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_SERVLET_ANNOTATION
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JavaSearchMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiFelixAnnotationsMixin
import com.aemtools.test.util.notNull
import com.intellij.psi.PsiField

/**
 * @author Dmytro Primshyts
 */
class JavaPsiUtilTest : BaseLightTest(),
    JavaSearchMixin,
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
      val psiClass = psiClass("com.test.MyService")

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
      val psiClass = psiClass("com.test.MyServlet")

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
      val psiClass = psiClass("com.test.MyFilter")

      assertTrue("The class annotated with SlingFilter annotation should be determined as OSGi service",
          psiClass.isOSGiService())
    }
  }

  fun `test SlingHealthCheck is OSGi service`() = fileCase {
    addSlingHealthCheckAnnotation()

    addClass("MyHealthCheck.java", """
      package com.test;

      import $SLING_HEALTH_CHECK_ANNOTATION;

      @SlingHealthCheck
      public class Test {}
    """)

    verify {
      val psiClass = psiClass("com.test.Test")

      assertTrue("The class annotated with SlingHealthCheck should be determined as OSGi service",
          psiClass.isOSGiService())
    }
  }

  fun `test isFelixProperty should return true for annotated public final string`() = fileCase {
    addFelixPropertyAnnotation()
    addClass("PropertyTest.java", """
      package com.test;

      import $FELIX_PROPERTY_ANNOTATION;

      public class PropertyTest {
        @Property
        public static final String PROPERTY = "property";
      }
    """)

    verify {
      val psiClass = psiClass("com.test.PropertyTest")

      val field by notNull {
        psiClass.findChildrenByType(PsiField::class.java)
            .firstOrNull()
      }

      assertTrue(field.isFelixProperty())
    }

  }

  // todo add test for declarative OSGi service declaration

}
