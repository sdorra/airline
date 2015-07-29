package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that indicates that you only permit one option from some set of
 * options to be present, the set of options are identified by a user defined
 * tag.
 * <p>
 * By using the same tag across several annotated options you can state that you
 * require only one of those options to be present. If you require one/more from
 * some set of options you should instead use the less restrictive
 * {@link RequireSome}
 * </p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface RequireOnlyOne {

    /**
     * Provides a tag used to identify some set of options
     * 
     * @return Tag
     */
    String tag() default "";
}
