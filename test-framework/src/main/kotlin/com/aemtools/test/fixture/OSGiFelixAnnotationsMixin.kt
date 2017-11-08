package com.aemtools.test.fixture

import com.aemtools.test.base.model.fixture.ITestFixture

/**
 * @author Dmytro Troynikov
 */
interface OSGiFelixAnnotationsMixin {

  /**
   * Add Felix OSGi service annotation to current fixture.
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addFelixServiceAnnotation() =
      this.addClass("org/apache/felix/scr/annotations/Service.java", """
        package org.apache.felix.scr.annotations;

        public @interface Service {}
      """)

  /**
   * Add Sling Filter annotation to current fixture.
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addFelixSlingFilterAnnotation() =
      this.addClass("org/apache/felix/src/annotations/sling/SlingFilter.java", """
        package org.apache.felix.scr.annotations.sling;

        public @interface SlingFilter {}
      """)

  /**
   * Add Sling Servlet annotation to current fixture.
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addFelixSlingServletAnnotation() =
      this.addClass("org/apache/felix/scr/annotations/sling/SlingServlet.java", """
        package org.apache.felix.scr.annotations.sling;

        public @interface SlingServlet {}
      """)

  fun ITestFixture.addFelixPropertyAnnotation() =
      this.addClass("org/apache/felix/scr/annotations/Property.java", """
        package org.apache.felix.scr.annotations;

        public @interface Property {}
      """)

}
