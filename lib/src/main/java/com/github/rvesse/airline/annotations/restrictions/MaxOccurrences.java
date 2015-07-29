package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface MaxOccurrences {

    /**
     * The maximum number of occurrences for this option
     * <p>
     * Zero or negative values are ignored
     * </p>
     * 
     * @return Max occurrences
     */
    public int occurrences() default 0;
}
