package com.aemtools.lang.htl.psi.pattern

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.lang.htl.service.HtlDetectionService
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
  fun testHtlFilePatternTestNegativeWhenFileInNotUnderJcrContent() = testCondition(
      HtlFilePattern,
      "",
      false,
      false,
      {
        HtlDetectionService.markAllInTest = false
        this.addHtml("/test.html", CARET)
      },
      {
        openedFile()
      },
      {
        HtlDetectionService.markAllInTest = true
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
