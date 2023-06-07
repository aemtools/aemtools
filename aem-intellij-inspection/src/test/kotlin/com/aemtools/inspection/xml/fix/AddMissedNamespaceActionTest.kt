package com.aemtools.inspection.xml.fix

import com.aemtools.inspection.xml.AemMissedCqNamespaceInspection
import com.aemtools.test.fix.BaseFixTest

class AddMissedNamespaceActionTest : BaseFixTest() {

  fun testCqDialog() = fixTest {
    inspection = AemMissedCqNamespaceInspection::class.java
    fixName = "Add missed 'cq' namespace"

    before = xml("_cq_dialog.xml", """
<?xml version="1.0" encoding="UTF-8"?>
<jcr:${CARET}root xmlns:jcr="http://www.jcp.org/jcr/1.0"
          jcr:primaryType="nt:unstructured">
</jcr:root>
    """.trimIndent())

    after = xml("_cq_dialog.xml", """
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0"
          jcr:primaryType="nt:unstructured" xmlns:cq="http://www.day.com/jcr/cq/1.0">
</jcr:root>
    """.trimIndent())
  }

  fun testCqEditConfig() = fixTest {
    inspection = AemMissedCqNamespaceInspection::class.java
    fixName = "Add missed 'cq' namespace"

    before = xml("_cq_editConfig.xml", """
<?xml version="1.0" encoding="UTF-8"?>
<jcr:${CARET}root xmlns:jcr="http://www.jcp.org/jcr/1.0"
          jcr:primaryType="nt:unstructured">
</jcr:root>
    """.trimIndent())

    after = xml("_cq_editConfig.xml", """
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0"
          jcr:primaryType="nt:unstructured" xmlns:cq="http://www.day.com/jcr/cq/1.0">
</jcr:root>
    """.trimIndent())
  }
}
