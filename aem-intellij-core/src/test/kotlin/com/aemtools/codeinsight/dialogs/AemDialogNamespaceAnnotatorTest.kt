package com.aemtools.codeinsight.dialogs

import com.aemtools.inspection.xml.AemMissedCqNamespaceInspection
import com.aemtools.test.base.BaseLightTest

class AemDialogNamespaceAnnotatorTest : BaseLightTest() {
  fun `test1`() {
    myFixture.enableInspections(AemMissedCqNamespaceInspection())

    myFixture.configureByText("_cq_dialog.xml", """
      <?xml version="1.0" encoding="UTF-8"?>
      <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0"
          xmlns:cq="http://www.day.com/jcr/cq/1.0"></jcr:root>
    """)
    myFixture.checkHighlighting(true, true, true)
  }
}
