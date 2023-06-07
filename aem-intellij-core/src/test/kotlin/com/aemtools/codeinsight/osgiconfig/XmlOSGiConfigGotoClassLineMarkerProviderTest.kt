package com.aemtools.codeinsight.osgiconfig

import com.aemtools.common.util.findChildrenByType
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.OSGiConfigFixtureMixin
import com.intellij.codeHighlighting.Pass
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlTag

/**
 * Test for [XmlOSGiConfigGotoClassLineMarkerProvider].
 *
 * @author Dmytro Primshyts
 */
class XmlOSGiConfigGotoClassLineMarkerProviderTest : BaseLightTest(),
    OSGiConfigFixtureMixin {

  var tested = XmlOSGiConfigGotoClassLineMarkerProvider()

  fun testShouldMarkOSGiServiceConfiguration() = fileCase {
    addEmptyOSGiConfigs("/config/com.test.Bean.xml")
    addClass("com.test.Bean.java", "package com.test; class Bean{}")

    verify {
      val config = FilenameIndex.getFilesByName(
          project,
          "com.test.Bean.xml",
          GlobalSearchScope.allScope(project)).first()

      val rootTag = config.findChildrenByType(XmlTag::class.java).first()

      val lineMarkerInfo = tested.getLineMarkerInfo(rootTag)

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
