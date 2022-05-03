package com.aemtools.codeinsight.osgiservice

import com.aemtools.codeinsight.osgiservice.markerinfo.OSGiPropertyMarkerInfo
import com.aemtools.test.base.BaseLightTest
import com.intellij.codeInsight.daemon.LineMarkerInfo
import org.assertj.core.api.Assertions
import org.assertj.core.groups.Tuple
import java.util.function.Function

abstract class BaseOSGiPropertyLineMarkerTest : BaseLightTest() {

  val lineMarkerElementTextAndTooltipExtractors = listOf<Function<LineMarkerInfo<*>, String?>>(
      java.util.function.Function { it.element?.text },
      java.util.function.Function { it.lineMarkerTooltip }
  )

  fun hasOSGiPropertyLineMarker(gutters: List<LineMarkerInfo.LineMarkerGutterIconRenderer<*>>,
                                extractors: List<Function<LineMarkerInfo<*>, *>>,
                                vararg expectedElements: Tuple) {
    Assertions.assertThat(gutters.map { it.lineMarkerInfo })
        .hasSize(expectedElements.size)
        .extracting(*extractors.toTypedArray())
        .containsExactlyInAnyOrder(*expectedElements)
  }

  fun getFixtureOsgiPropertyGutters(): List<LineMarkerInfo.LineMarkerGutterIconRenderer<*>> {
    val gutters = myFixture.findAllGutters()

    return gutters.mapNotNull {
      it as? LineMarkerInfo.LineMarkerGutterIconRenderer<*>
    }.filter {
      it.lineMarkerInfo is OSGiPropertyMarkerInfo
    }
  }

  fun hasNotOSGIPropertyLineMarker() {
    Assertions.assertThat(getFixtureOsgiPropertyGutters()).isEmpty();
  }
}

