package com.aemtools.lang.htl.psi.pattern

import com.aemtools.blocks.pattern.BasePatternsTest

import com.aemtools.constant.const.JCR_ROOT

/**
 * @author Dmytro Troynikov
 */
class HtlFilePatternTest : BasePatternsTest() {

  fun testHtlFilePatternTestPositive() = testCondition(
      HtlFilePattern,
      "",
      true,
      false,
      {
        this.addHtml("/$JCR_ROOT/test.html", CARET)
      },
      {
        openedFile()
      }
  )

  fun testHtlFilePatternsTestNegative() = testCondition(
      HtlFilePattern,
      "",
      false,
      false,
      {
        addXml("/$JCR_ROOT/test.xml", CARET)
      },
      {
        openedFile()
      }
  )

}
