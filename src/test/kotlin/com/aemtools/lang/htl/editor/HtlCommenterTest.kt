package com.aemtools.lang.htl.editor

import com.aemtools.blocks.base.BaseLightTest
import com.intellij.openapi.actionSystem.IdeActions

/**
 * @author Dmytro Troynikov
 */
class HtlCommenterTest : BaseLightTest(false) {

  fun testHtlCommenter() {
    myFixture.configureByText("test.html", CARET)
    myFixture.performEditorAction(IdeActions.ACTION_COMMENT_BLOCK)
    myFixture.checkResult("<!--/*  */-->")
  }

}
