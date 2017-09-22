package com.aemtools.lang.clientlib

import com.aemtools.blocks.base.BaseLightTest
import com.intellij.codeInsight.actions.MultiCaretCodeInsightAction
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.IdeActions

/**
 * @author Dmytro Troynikov
 */
class CdCommenterTest : BaseLightTest(false) {

  fun testLineComment() = fileCase {
    addFile("js.txt", """
${CARET}my/path
        """)

    verify {
      val action = ActionManager.getInstance()
          .getAction(IdeActions.ACTION_COMMENT_LINE)
          as MultiCaretCodeInsightAction
      action.actionPerformedImpl(project, editor)

      myFixture.checkResult("""
# my/path
        """)
    }
  }

  fun testLineUncomment() = fileCase {
    addFile("js.txt", """
            $CARET# my/path
            """)

    verify {
      val action = ActionManager.getInstance()
          .getAction(IdeActions.ACTION_COMMENT_LINE)
          as MultiCaretCodeInsightAction
      action.actionPerformedImpl(project, editor)

      myFixture.checkResult("""
            my/path
            """)
    }
  }

}
