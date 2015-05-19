package com.github.rvesse.airline.args;

import java.util.List;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.DefaultOption;
import com.github.rvesse.airline.Option;

@Command(name = "ArgsDefaultOption")
public class ArgsDefaultOptionBadArity {

    @Option(name = "--test", arity = 2, required = true)
    @DefaultOption
    public List<String> arg;
}
