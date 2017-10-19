package com.aemtools.ide

import com.aemtools.blocks.base.BaseLightTest
import com.intellij.testFramework.PlatformTestUtil

/**
 * @author Dmytro Troynikov
 */
class HtlStructureViewProviderTest : BaseLightTest(false) {

  fun testHtmlStructureViewShouldBePreserved() {
    myFixture.configureByText("test.html", """
            <div data-sly-use.bean="bean">
                $DOLLAR{'some expression'}
            </div>
        """)

    myFixture.testStructureView { component ->
      PlatformTestUtil.assertTreeEqual(
          component.tree,
          "-test.html\n div\n"
      )
    }
  }

}
