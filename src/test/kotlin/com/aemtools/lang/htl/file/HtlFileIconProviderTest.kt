package com.aemtools.lang.htl.file

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.lang.htl.icons.HtlIcons
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Dmytro Troynikov
 */
class HtlFileIconProviderTest : BaseLightTest(false) {

  fun testShouldProvideIconForHtlFile() = fileCase {
    addHtml("test.html", CARET)
    verify {
      val file = myFixture.file

      assertThat(HtlFileIconProvider().getIcon(file, 0))
          .isEqualTo(HtlIcons.HTL_FILE_ICON)
    }
  }

  fun testShouldNotMatchNonHtl() = fileCase {
    addXml("test.xml", CARET)
    verify {
      val file = myFixture.file

      assertThat(HtlFileIconProvider().getIcon(file, 0))
          .isNull()
    }
  }

}
