package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation that indicates that an options raw values must match a given
 * regular expression
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Pattern {

    /**
     * The regular expression that must be matched
     * 
     * @return Pattern
     */
    String pattern() default ".*";

    /**
     * The flags for the regular expression
     * 
     * @return Flags
     */
    int flags() default 0;
}
