package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "ArgsDefaultOptionAndArguments")
public class ArgsDefaultOptionAndArguments {

    @Option(name = "--test", arity = 1, required = true)
    @DefaultOption
    public String option;
    
    @Arguments
    public String arg;
}
