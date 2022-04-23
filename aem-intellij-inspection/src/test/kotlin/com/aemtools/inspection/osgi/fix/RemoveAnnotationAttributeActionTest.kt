package com.aemtools.inspection.osgi.fix

import com.aemtools.inspection.osgi.MessedFelixComponentMetatypeInspection
import com.aemtools.test.fix.BaseFixTest
import com.aemtools.test.fix.FileDescriptor
import com.aemtools.test.fixture.OSGiConfigTestClassesMixin

/**
 * Test for [RemoveAnnotationAttributeAction].
 *
 * @author Kostiantyn Diachenko
 */
class RemoveAnnotationAttributeActionTest : BaseFixTest(), OSGiConfigTestClassesMixin {
  fun testRemoveAnnotationAttributeAction() = fixTest {
    inspection = MessedFelixComponentMetatypeInspection::class.java
    fixName = "Remove 'metatype' attribute"

    myFixture.addFelixComponentClass()
    myFixture.addFelixPropertyAnnotation()

    before = FileDescriptor("TestService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Component;

      @Component(
              label = "Test service",
              description = "Test service description",
              immediate = true,
              metatype = ${CARET}true)
      public class TestService {
      }
    """)

    after = FileDescriptor("TestService.java", """
      package com.test;

      import org.apache.felix.scr.annotations.Component;

      @Component(
              label = "Test service",
              description = "Test service description",
              immediate = true
      )
      public class TestService {
      }
    """)
  }
}
