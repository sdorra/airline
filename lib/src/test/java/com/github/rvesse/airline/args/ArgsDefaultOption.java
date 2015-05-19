package com.github.rvesse.airline.args;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.DefaultOption;
import com.github.rvesse.airline.Option;

@Command(name = "ArgsDefaultOption")
public class ArgsDefaultOption {

    @Option(name = "--test", arity = 1, required = true)
    @DefaultOption
    public String arg;
}
