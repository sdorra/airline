package com.github.rvesse.airline.annotations.help;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface Examples {

    /**
     * An array of lines of text to provide a series of example usages of the
     * command. Each example may be provided with a description using the
     * {@link #descriptions()} property. The data in these two properties is
     * collated based on array indices.
     * 
     * @return Examples
     */
    public String[] examples() default {};

    /**
     * An array of paragraphs of text where each paragraph described the
     * corresponding example given in the {@link #examples()} property. The data
     * in these two properties is collated based on array indices.
     * 
     * @return Descriptions
     */
    public String[] descriptions() default {};
}
