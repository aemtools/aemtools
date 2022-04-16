package com.aemtools.test.fixture

import com.aemtools.test.base.model.fixture.ITestFixture

/**
 * @author Kostiantyn Diachenko
 */
interface OSGiDsAnnotationsMixin {
  /**
   * Add OSGi service component annotation to current fixture.
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addComponentAnnotation() =
      this.addClass("org/osgi/service/component/annotations/Component.java", """
        package org.osgi.service.component.annotations;

        public @interface Component {}
      """)

  /**
   * Add OSGi service object class definition (OCD) annotation to current fixture.
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addObjectClassDefinitionAnnotation() =
      this.addClass("org/osgi/service/metatype/annotations/ObjectClassDefinition.java", """
        package org.osgi.service.metatype.annotations;

        public @interface ObjectClassDefinition {}
      """)

  /**
   * Add OSGi OCD attribute definition annotation to current fixture.
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addAttributeDefinitionAnnotation() =
      this.addClass("org/osgi/service/metatype/annotations/AttributeDefinition.java", """
        package org.osgi.service.metatype.annotations;

        public @interface AttributeDefinition {}
      """)

  /**
   * Add OSGi designate annotation to current fixture.
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addDesignateAnnotation() =
      this.addClass("org/osgi/service/metatype/annotations/Designate.java", """
        package org.osgi.service.metatype.annotations;

        public @interface Designate {
            Class<?> ocd();
        }
      """)
}
