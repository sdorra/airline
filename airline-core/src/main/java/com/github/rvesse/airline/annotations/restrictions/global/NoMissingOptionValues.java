package com.github.rvesse.airline.annotations.restrictions.global;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies that a CLI does not permit options to be specified without their
 * values
 * 
 * @author rvesse
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface NoMissingOptionValues {

}
