package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "ArgsDefaultOption")
public class ArgsDefaultOption {

    @Option(name = "--test", arity = 1, required = true)
    @DefaultOption
    public String arg;
}
