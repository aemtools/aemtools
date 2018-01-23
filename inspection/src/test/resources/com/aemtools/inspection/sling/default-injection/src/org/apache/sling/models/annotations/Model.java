package org.apache.sling.models.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a class as adaptable via Sling Models.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {

    /**
     * @return List of classes from which can be adapted.
     */
    public Class<?>[] adaptables();

    /**
     * @return List of classes to which can be adapted. If missing, the class that is annotated is used.
     *   If classes are given, they have to be either the annotated class itself, or interfaces or super classes of the class.
     */
    public Class<?>[] adapters() default {};

    /**
     * @return Default injection strategy (optional or required)
     */
    public DefaultInjectionStrategy defaultInjectionStrategy() default DefaultInjectionStrategy.REQUIRED;

    /**
     * @return Condition that is displayed in the felix console adapter plugin
     */
    public String condition() default "";

}
