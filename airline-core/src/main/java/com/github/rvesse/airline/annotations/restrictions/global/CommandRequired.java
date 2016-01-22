package com.github.rvesse.airline.annotations.restrictions.global;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks that a CLI requires that a command be specified.
 * <p>
 * Note that if the group specified by the user has a default command then this
 * restriction has no effect.
 * </p>
 * 
 * @author rvesse
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface CommandRequired {

}
