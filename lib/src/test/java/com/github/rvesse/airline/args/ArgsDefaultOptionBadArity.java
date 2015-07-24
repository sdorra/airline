package com.github.rvesse.airline.args;

import java.util.List;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "ArgsDefaultOption")
public class ArgsDefaultOptionBadArity {

    @Option(name = "--test", arity = 2, required = true)
    @DefaultOption
    public List<String> arg;
}
