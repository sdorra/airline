package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface MinOccurrences {

    /**
     * The minimum number of occurrences for this option
     * <p>
     * Zero or negative values are ignored
     * </p>
     * 
     * @return Min occurrences
     */
    public int occurrences() default 0;
}
