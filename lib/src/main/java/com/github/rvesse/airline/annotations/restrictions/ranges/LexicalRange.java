package com.github.rvesse.airline.annotations.restrictions.ranges;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that marks values as being restricted to a given lexical range
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface LexicalRange {

    /**
     * Minimum value, the empty string is interpreted as no minimum
     * 
     * @return Minimum value
     */
    String min() default "";

    /**
     * Maximum value, the empty string is interpreted as no maximum
     * 
     * @return Maximum value
     */
    String max() default "";

    /**
     * Whether the minimum is inclusive
     * 
     * @return True if inclusive, false otherwise
     */
    boolean minInclusive() default true;

    /**
     * Whether the maximum is inclusive
     * 
     * @return True if inclusive, false otherwise
     */
    boolean maxInclusive() default true;

    /**
     * The locale used for comparisons
     * 
     * @return Locale BCP47 tag
     */
    String locale() default "en";
}
