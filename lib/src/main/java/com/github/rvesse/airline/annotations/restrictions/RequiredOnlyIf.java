package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation that indicates that an option/arguments is required only if
 * some other option/options are present
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface RequiredOnlyIf {

    /**
     * The name(s) of the other option(s) that must be present for this to be
     * a required option
     * 
     * @return Names
     */
    String[] names() default {};
}
