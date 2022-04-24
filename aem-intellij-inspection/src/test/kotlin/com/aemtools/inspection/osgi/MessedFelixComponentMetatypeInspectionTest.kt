package com.aemtools.inspection.osgi

import com.aemtools.test.HtlTestCase
import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.fixture.OSGiConfigTestClassesMixin
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import java.io.File

/**
 * Test for [MessedFelixComponentMetatypeInspection].
 *
 * @author Kostiantyn Diachenko
 */
class MessedFelixComponentMetatypeInspectionTest : BaseLightTest(true), OSGiConfigTestClassesMixin {
  override fun getTestDataPath(): String {
    return File(HtlTestCase.testResourcesPath).absolutePath
  }

  fun testInspectionWhenNoFelixProperties() {
    with(myFixture) {
      addFelixComponentClass()
      addFelixPropertyAnnotation()
    }

    myFixture.enableInspections(MessedFelixComponentMetatypeInspection())

    myFixture.testInspection("com/aemtools/inspection/java/osgi/messed-felix-component-metatype/no-properties",
        LocalInspectionToolWrapper(MessedFelixComponentMetatypeInspection()))
  }

  fun testInspectionWhenOnlyPrivateProperties() {
    with(myFixture) {
      addFelixComponentClass()
      addFelixPropertyAnnotation()
    }

    myFixture.enableInspections(MessedFelixComponentMetatypeInspection())

    myFixture.testInspection("com/aemtools/inspection/java/osgi/messed-felix-component-metatype/private-properties",
        LocalInspectionToolWrapper(MessedFelixComponentMetatypeInspection()))
  }

  fun testInspectionWhenThereAreFieldAndClassProperties() {
    with(myFixture) {
      addFelixComponentClass()
      addFelixPropertyAnnotation()
      addFelixPropertiesClass()
    }

    myFixture.enableInspections(MessedFelixComponentMetatypeInspection())

    myFixture.testInspection("com/aemtools/inspection/java/osgi/messed-felix-component-metatype/mixed-properties",
        LocalInspectionToolWrapper(MessedFelixComponentMetatypeInspection()))
  }

  fun testInspectionWhenOnlyPrivatePropertyOnClassLevel() {
    with(myFixture) {
      addFelixComponentClass()
      addFelixPropertyAnnotation()
      addFelixPropertiesClass()
    }

    myFixture.enableInspections(MessedFelixComponentMetatypeInspection())

    myFixture.testInspection(
        "com/aemtools/inspection/java/osgi/messed-felix-component-metatype/only-class-level-properties",
        LocalInspectionToolWrapper(MessedFelixComponentMetatypeInspection()))
  }

}
