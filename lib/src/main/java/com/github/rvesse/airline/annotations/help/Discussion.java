package com.github.rvesse.airline.annotations.help;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface Discussion {

    /**
     * An array of paragraphs of text that provides an extended discussion on
     * the behaviour of the command. Discussion typically goes into much greater
     * detail about exact behaviour of commands particularly where complex
     * commands may behave differently depending on the option combinations
     * used.
     * 
     * @return Command discussion
     */
    public String[] paragraphs() default {};
}
