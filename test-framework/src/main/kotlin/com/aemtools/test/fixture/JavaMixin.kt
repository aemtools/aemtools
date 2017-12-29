package com.aemtools.test.fixture

import com.aemtools.test.base.model.fixture.ITestFixture

/**
 * @author Dmytro Troynikov
 */
interface JavaMixin {

  /**
   * Add `java.lang.String` to current fixture.
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.javaLangString() {
    addClass("String.java", """
        package java.lang;

        public class String {}
    """)
  }

}
