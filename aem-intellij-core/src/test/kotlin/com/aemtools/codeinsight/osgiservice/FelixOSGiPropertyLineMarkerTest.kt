package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.FelixOSGiPropertyMarkerInfo
import com.aemtools.common.constant.const.java.FELIX_PROPERTY_ANNOTATION
import com.aemtools.common.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.JavaMixin
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.aemtools.test.fixture.OSGiFelixAnnotationsMixin
import com.intellij.codeInsight.daemon.LineMarkerInfo
import org.assertj.core.api.Assertions.assertThat

/**
 * Test for [FelixOSGiPropertyLineMarker].
 *
 * @author Dmytro Troynikov
 */
class FelixOSGiPropertyLineMarkerTest : BaseLightTest(),
    OSGiConfigFixtureMixin,
    OSGiFelixAnnotationsMixin,
    JavaMixin {

  fun testFelixPropertiesShouldBeMarked() = fileCase {
    addFelixServiceAnnotation()
    addFelixPropertyAnnotation()

    javaLangString()

    addClass("MyService.java", """
      package com.test;

      import $FELIX_SERVICE_ANNOTATION;
      import $FELIX_PROPERTY_ANNOTATION;

      @Service
      public class MyService {

        @Property
        private static final String ${CARET}TEST = "test.property"

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

      assertThat(felixGutter.lineMarkerTooltip)
          .isEqualTo("OSGi Property")

    }
  }

}
