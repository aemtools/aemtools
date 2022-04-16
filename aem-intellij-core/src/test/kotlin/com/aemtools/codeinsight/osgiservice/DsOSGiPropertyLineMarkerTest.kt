package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.FelixOSGiPropertyMarkerInfo
import com.aemtools.common.constant.const
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JavaMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiDsAnnotationsMixin
import com.intellij.codeInsight.daemon.LineMarkerInfo
import org.assertj.core.api.Assertions

/**
 * Test for [DsOSGiPropertyLineMarker].
 *
 * @author Kostiantyn Diachenko
 */
class DsOSGiPropertyLineMarkerTest : BaseLightTest(),
    OSGiConfigFixtureMixin,
    OSGiDsAnnotationsMixin,
    JavaMixin {

  fun testDsPropertiesShouldBeMarkedWhenOsgiConfigAsSeparateAnnotation() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("Config.java", """
      package com.test;

      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};
      
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

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_DESIGNATE_ANNOTATION};
      import com.test.Config;

      @Component
      @Designate(ocd = Config.class)
      public class MyService {

      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    osgiConfig("/config/author/com.test.MyService.xml", """
      testProperty="test value"
    """)

    verify {
      val gutters = myFixture.findAllGutters()

      val felixGutter = gutters.mapNotNull {
        it as? LineMarkerInfo.LineMarkerGutterIconRenderer<*>
      }.find {
        it.lineMarkerInfo is FelixOSGiPropertyMarkerInfo
      }?.lineMarkerInfo ?: throw AssertionError("Unable to find Felix gutter")

      Assertions.assertThat(felixGutter.lineMarkerTooltip)
          .isEqualTo("OSGi Property")

    }
  }

  fun testDsPropertiesShouldBeMarkedWhenOsgiConfigAsInnerAnnotation() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("MyService.java", """
      package com.test;

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_DESIGNATE_ANNOTATION};
      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};

      @Component
      @Designate(ocd = MyService.Config.class)
      public class MyService {
      
        @ObjectClassDefinition(name = "Configuration")
        public @interface Config {
  
          @AttributeDefinition(
              name = "Test property",
              description = "Test property description")
          String ${CARET}testProperty();
        }
      }
    """)

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    osgiConfig("/config/author/com.test.MyService.xml", """
      testProperty="test value"
    """)

    verify {
      val gutters = myFixture.findAllGutters()

      val felixGutter = gutters.mapNotNull {
        it as? LineMarkerInfo.LineMarkerGutterIconRenderer<*>
      }.find {
        it.lineMarkerInfo is FelixOSGiPropertyMarkerInfo
      }?.lineMarkerInfo ?: throw AssertionError("Unable to find Felix gutter")

      Assertions.assertThat(felixGutter.lineMarkerTooltip)
          .isEqualTo("OSGi Property")

    }
  }

  /**
   * Note: Class file doesn't provide PsiJavaCodeReferenceElement
   */
  fun testDsPropertiesShouldBeMarkedWhenOsgiConfigAsInnerAnnotationInClassFile() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("MyService.class", """
      package com.test;

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_DESIGNATE_ANNOTATION};
      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};

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

    addEmptyOSGiConfigs(
        "/config/com.test.MyService.xml"
    )

    osgiConfig("/config/author/com.test.MyService.xml", """
      test.property="test value"
    """)

    verify {
      val gutters = myFixture.findAllGutters()

      val felixGutter = gutters.mapNotNull {
        it as? LineMarkerInfo.LineMarkerGutterIconRenderer<*>
      }.find {
        it.lineMarkerInfo is FelixOSGiPropertyMarkerInfo
      }?.lineMarkerInfo ?: throw AssertionError("Unable to find Felix gutter")

      Assertions.assertThat(felixGutter.lineMarkerTooltip)
          .isEqualTo("OSGi Property")

    }
  }
}
