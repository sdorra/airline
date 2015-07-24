package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "ArgsMultipleDefaultOption")
public class ArgsMultipleDefaultOptions {

    @Option(name = "-a", arity = 1, required = true)
    @DefaultOption
    public String arg1;
    
    @Option(name = "-b", arity = 1, required = true)
    @DefaultOption
    public String arg2;
}
