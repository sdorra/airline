package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation denotes that at least one of some set of options are
 * required, the set of options are identified by a user defined tag.
 * <p>
 * By using the same tag across several annotated options you can state that you
 * require at least one of those options to be present. If you require only one
 * from some set of options you should instead use {@link RequireOnlyOne}
 * </p>
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface RequireSome {

    /**
     * Provides a tag used to identify some set of options
     * 
     * @return Tag
     */
    String tag() default "";
}
