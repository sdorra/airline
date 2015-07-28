package com.github.rvesse.airline.annotations.restrictions.ranges;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that marks values as being restricted to a given integer range
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface IntegerRange {

    int min() default Integer.MIN_VALUE;
    
    int max() default Integer.MAX_VALUE;
    
    boolean minInclusive() default true;
    
    boolean maxInclusive() default true;
}
