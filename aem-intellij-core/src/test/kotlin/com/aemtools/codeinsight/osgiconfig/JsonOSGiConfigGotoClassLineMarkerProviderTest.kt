package com.aemtools.codeinsight.osgiconfig

import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.toPsiFile
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.intellij.codeHighlighting.Pass
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

class JsonOSGiConfigGotoClassLineMarkerProviderTest : BaseLightTest(),
    OSGiConfigFixtureMixin {
  var tested = JsonOSGiConfigGotoClassLineMarkerProvider()

  fun testShouldMarkOSGiServiceConfiguration() = fileCase {
    addEmptyOSGiConfigs("/config/com.test.Bean.cfg.json")
    addClass("com.test.Bean.java", "package com.test; class Bean{}")

    verify {
      val config = FilenameIndex.getVirtualFilesByName(
          "com.test.Bean.cfg.json",
          GlobalSearchScope.allScope(project)).first()?.toPsiFile(project)

      val rootJsonObject = config.findChildrenByType(JsonObject::class.java).first()

      val lineMarkerInfo = tested.getLineMarkerInfo(rootJsonObject)

      val element = lineMarkerInfo?.element
          ?: throw AssertionError("Element is null")

      assertTrue(element is LeafElement)
      assertNotNull(lineMarkerInfo)

      assertEquals(
          "Open associated OSGi service",
          lineMarkerInfo.lineMarkerTooltip
      )

      assertEquals(
          Pass.LINE_MARKERS,
          lineMarkerInfo.updatePass
      )

      assertEquals(
          GutterIconRenderer.Alignment.CENTER,
          lineMarkerInfo.createGutterRenderer()?.alignment
      )

      val navigationHandler = lineMarkerInfo.navigationHandler

      assertTrue(
          GutterIconNavigationHandler::class.java.isAssignableFrom(
              navigationHandler?.javaClass)
      )
    }
  }
}

