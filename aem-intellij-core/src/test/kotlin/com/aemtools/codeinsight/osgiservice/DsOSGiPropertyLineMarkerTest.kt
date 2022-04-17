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

  fun `test marker info DS OSGi Config with OCD in separate class`() = fileCase {
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

  fun `test marker info DS OSGi Config with OCD as inner class`() = fileCase {
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
  fun `test marker info DS OSGi Config when OCD as inner annotation in _class file`() = fileCase {
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

  fun `test no marker info DS OSGi Config when OCD in not marked as OCD`() = fileCase {
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
      val gutters = myFixture.findAllGutters()

      Assertions.assertThat(gutters)
          .isEmpty()
    }
  }

  fun `test no marker info DS OSGi Config when OCD method doesn't have AttributeDefinition annotation`() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()

    javaLangString()

    addClass("MyService.java", """
      package com.test;

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_DESIGNATE_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};

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
      val gutters = myFixture.findAllGutters()

      Assertions.assertThat(gutters)
          .isEmpty()
    }
  }

  fun `test no marker info DS OSGi Config when no config files for current OCD`() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("MyService.class", """
      package com.test;

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_DESIGNATE_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};
      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};

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
      val gutters = myFixture.findAllGutters()

      Assertions.assertThat(gutters)
          .isEmpty()
    }
  }

  fun `test no marker info DS OSGi Config when component doesn't have Designate annotation`() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("MyService.java", """
      package com.test;

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};
      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};

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
      val gutters = myFixture.findAllGutters()

      Assertions.assertThat(gutters)
          .isEmpty()
    }
  }

  fun `test no marker info DS OSGi Config when it is referenced not via Designate annotation`() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("MyService.class", """
      package com.test;

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};
      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};

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
      val gutters = myFixture.findAllGutters()

      Assertions.assertThat(gutters)
          .isEmpty()
    }
  }

  fun `test no marker info DS OSGi Config when component is referenced to another OCD`() = fileCase {
    addComponentAnnotation()
    addDesignateAnnotation()
    addObjectClassDefinitionAnnotation()
    addAttributeDefinitionAnnotation()

    javaLangString()

    addClass("Config.java", """
      package com.test;

      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};
      
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

      import ${const.java.DS_COMPONENT_ANNOTATION};
      import ${const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION};
      import ${const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION};
      import ${const.java.DS_DESIGNATE_ANNOTATION};

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
      val gutters = myFixture.findAllGutters()

      Assertions.assertThat(gutters)
          .isEmpty()
    }
  }
}
