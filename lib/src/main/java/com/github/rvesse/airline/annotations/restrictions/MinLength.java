package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface MinLength {

    /**
     * Minimum required length
     * 
     * @return Minimum length
     */
    public int length() default 0;
}
