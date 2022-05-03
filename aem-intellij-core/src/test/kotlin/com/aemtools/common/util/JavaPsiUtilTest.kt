package com.aemtools.common.util

import com.aemtools.common.constant.const.java.FELIX_PROPERTY_ANNOTATION
import com.aemtools.common.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_FILTER_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_HEALTH_CHECK_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_SERVLET_ANNOTATION
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JavaSearchMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiDsAnnotationsMixin
import com.aemtools.test.fixture.OSGiFelixAnnotationsMixin
import com.aemtools.test.util.notNull
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMethod

/**
 * @author Dmytro Primshyts
 */
class JavaPsiUtilTest : BaseLightTest(),
    JavaSearchMixin,
    OSGiConfigFixtureMixin,
    OSGiFelixAnnotationsMixin,
    OSGiDsAnnotationsMixin {

  fun `test is OSGi Service Felix annotated`() = fileCase {
    addFelixServiceAnnotation()
    addClass("MyService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Service;

      @Service
      public class MyService {}
    """)

    verify {
      val psiClass = psiClass("com.test.MyService")

      assertTrue("The class should be determined as OSGi Felix service", psiClass.isOSGiService())
    }
  }

  fun `test Sling Servlet is OSGi Service`() = fileCase {
    addFelixSlingServletAnnotation()
    addClass("MyServlet.java", """
      package com.test;

      import org.apache.felix.scr.annotations.sling.SlingServlet;

      @SlingServlet
      public class MyServlet {}
    """)

    verify {
      val psiClass = psiClass("com.test.MyServlet")

      assertTrue("The class annotated with SlingServlet annotation should be determined as OSGi service",
          psiClass.isOSGiService())
    }
  }

  fun `test Sling Filter is OSGi Service`() = fileCase {
    addFelixSlingFilterAnnotation()
    addClass("MyFilter.java", """
      package com.test;

      import org.apache.felix.scr.annotations.sling.SlingFilter;

      @SlingFilter
      public class MyFilter {}
    """)

    verify {
      val psiClass = psiClass("com.test.MyFilter")

      assertTrue("The class annotated with SlingFilter annotation should be determined as OSGi service",
          psiClass.isOSGiService())
    }
  }

  fun `test Sling Health Check is OSGi service`() = fileCase {
    addSlingHealthCheckAnnotation()

    addClass("MyHealthCheck.java", """
      package com.test;

      import org.apache.sling.hc.annotations.SlingHealthCheck;

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

      import org.apache.felix.scr.annotations.Property;

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

  fun `test is declarative OSGi service annotated`() = fileCase {
    addComponentAnnotation()

    addClass("ServiceTest.java", """
      package com.test;

      import org.osgi.service.component.annotations.Component;

      @Component
      public class ServiceTest {
        @Property
        public static final String PROPERTY = "property";
      }
    """)

    verify {
      val psiClass = psiClass("com.test.ServiceTest")

      assertTrue("The class should be determined as OSGi DS service", psiClass.isOSGiService())
    }
  }

  fun `test is declarative OSGi service Object Class Method property annotated`() = fileCase {
    addAttributeDefinitionAnnotation()
    addObjectClassDefinitionAnnotation()

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

    verify {
      val psiClass = psiClass("com.test.Config")

      val method by notNull {
        psiClass.findChildrenByType(PsiMethod::class.java)
            .firstOrNull()
      }

      assertTrue("The method should be determined as OSGi OCD property", method.isDsOSGiConfigProperty())
    }
  }

}
