package com.aemtools.test.fixture

import com.intellij.psi.PsiClass
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

/**
 * Mixin with Felix annotations for tests.
 *
 * @author Kostiantyn Diachenko
 */
interface OSGiConfigTestClassesMixin {
  fun JavaCodeInsightTestFixture.addFelixPropertyAnnotation(): PsiClass =
      addClass("""
          package org.apache.felix.scr.annotations;
          
          public @interface Property {
              String name() default "";
              String label() default "";
              String description() default "";
              String[] value() default {};
              Class<?>[] classValue() default {};
              long[] longValue() default {};
              double[] doubleValue() default {};
              float[] floatValue() default {};
              int[] intValue() default {};
              byte[] byteValue() default {};
              char[] charValue() default {};
              boolean[] boolValue() default {};
              short[] shortValue() default {};
              int cardinality() default 0;
              PropertyUnbounded unbounded() default PropertyUnbounded.DEFAULT;
              boolean propertyPrivate() default false;
              PropertyOption[] options() default {};
          }
        """)

  fun JavaCodeInsightTestFixture.addFelixComponentClass(): PsiClass =
      addClass("""
        package org.apache.felix.scr.annotations;
        
        public @interface Component {
            String name() default "";
            String label() default "";
            String description() default "";
            boolean enabled() default true;
            String factory() default "";
            boolean immediate() default false;
            boolean inherit() default true;
            boolean metatype() default false;
            boolean componentAbstract() default false;
            boolean ds() default true;
            String specVersion() default "";
            boolean createPid() default true;
            boolean configurationFactory() default false;
            ConfigurationPolicy policy() default ConfigurationPolicy.OPTIONAL;
            String configurationPid() default "";
        }
        """)
}
