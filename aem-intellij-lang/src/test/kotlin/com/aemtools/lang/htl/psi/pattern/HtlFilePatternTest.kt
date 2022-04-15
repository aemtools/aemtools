package com.aemtools.lang.htl.psi.pattern

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.pattern.BasePatternsTest

/**
 * @author Dmytro Primshyts
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
