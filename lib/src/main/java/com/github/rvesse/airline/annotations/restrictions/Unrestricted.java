package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation that indicates that no restrictions should apply
 * <p>
 * While this may seem slightly strange this is needed because when overriding
 * options the restrictions lowest in the hierarchy apply so if you want to
 * remove parent restrictions then you have to explicitly state that the option
 * is unrestricted
 * </p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Unrestricted {

}
