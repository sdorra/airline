package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Required;

@Command(name = "ArgsMultipleDefaultOption")
public class ArgsMultipleDefaultOptions {

    @Option(name = "-a", arity = 1)
    @Required
    @DefaultOption
    public String arg1;
    
    @Option(name = "-b", arity = 1)
    @Required
    @DefaultOption
    public String arg2;
}
