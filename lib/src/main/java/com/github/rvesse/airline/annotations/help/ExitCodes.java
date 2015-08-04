package com.github.rvesse.airline.annotations.help;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that indicates the exit codes for a command
 * @author rvesse
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface ExitCodes {

    /**
     * The exit codes that this command may return, meanings of the exit codes
     * may be given using the {@link #descriptions()} property. The data in
     * these two properties is collated based on array indices.
     * 
     * @return Array of exit codes
     */
    int[] codes() default {};

    /**
     * Descriptions of the meanings of the exit codes this command may return,
     * the exit codes are given by the {@link #codes()} property. The data
     * in these two properties is collated based on array indices.
     * 
     * @return
     */
    String[] descriptions() default {};
}
