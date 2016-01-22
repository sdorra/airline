package com.github.rvesse.airline.annotations.restrictions.global;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.rvesse.airline.annotations.Arguments;

/**
 * Marks that a CLI does not permit any command line arguments that aren't
 * recognised as either options or arguments.
 * <p>
 * Note that if the specified command
 * has an {@link Arguments} annotated field then this restriction has no effect
 * because any unrecognised command line arguments are placed into the
 * {@linkplain Arguments} annotated field.
 * </p>
 * 
 * @author rvesse
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface NoUnexpectedArguments {

}
