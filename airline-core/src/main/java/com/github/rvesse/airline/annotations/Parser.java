package com.github.rvesse.airline.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.rvesse.airline.model.ParserMetadata;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Parser {

    boolean allowCommandAbbreviation() default false;
    
    boolean allowOptionAbbreviation() default false;
    
    String argumentsSeparator() default ParserMetadata.DEFAULT_ARGUMENTS_SEPARATOR;
    
    boolean aliasesMayChain() default false;
    
    boolean aliasesOverrideBuiltIns() default false;
    
    Alias[] aliases() default {};
    
    // TODO Fields for user defined aliases
}
