package com.aemtools.inspection.xml

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BasePlatformLightTest
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import java.io.File

class AemMissedCqNamespaceInspectionTest: BasePlatformLightTest() {
  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  override fun setUp() {
    super.setUp()
    myFixture.enableInspections(AemMissedCqNamespaceInspection())
  }

  fun testInspectionCqDialogInFolder() {
    myFixture.testInspection("com/aemtools/inspection/xml/missed-cq-namespace/test-cq-dialog-in-folder",
        LocalInspectionToolWrapper(AemMissedCqNamespaceInspection()))
  }

  fun testInspectionCqDialogAsFile() {
    myFixture.testInspection("com/aemtools/inspection/xml/missed-cq-namespace/test-cq-dialog-file",
        LocalInspectionToolWrapper(AemMissedCqNamespaceInspection()))
  }

  fun testInspectionFileVaultFile() {
    myFixture.testInspection("com/aemtools/inspection/xml/missed-cq-namespace/test-file-vault-file",
        LocalInspectionToolWrapper(AemMissedCqNamespaceInspection()))
  }

  fun testHighlightingCqDialog() {
    myFixture.configureByText("_cq_dialog.xml", """
<?xml version="1.0" encoding="UTF-8"?>
<<error descr="Missed 'cq' namespace">jcr:root</error> xmlns:jcr="http://www.jcp.org/jcr/1.0"
          jcr:primaryType="nt:unstructured">
</jcr:root>
    """.trimIndent())
    myFixture.testHighlighting()
  }

}
