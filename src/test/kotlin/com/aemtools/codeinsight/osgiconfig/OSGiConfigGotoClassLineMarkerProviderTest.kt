package com.aemtools.codeinsight.osgiconfig

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.fixture.OSGiConfigFixtureMixin
import com.aemtools.completion.util.findChildrenByType
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlTag

/**
 * Test for [OSGiConfigGotoClassLineMarkerProvider].
 *
 * @author Dmytro Troynikov
 */
class OSGiConfigGotoClassLineMarkerProviderTest : BaseLightTest(),
    OSGiConfigFixtureMixin {

  var tested = OSGiConfigGotoClassLineMarkerProvider()

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
    }
  }

}
