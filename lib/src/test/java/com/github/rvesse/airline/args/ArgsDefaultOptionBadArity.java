package com.github.rvesse.airline.args;

import java.util.List;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Required;

@Command(name = "ArgsDefaultOption")
public class ArgsDefaultOptionBadArity {

    @Option(name = "--test", arity = 2)
    @Required
    @DefaultOption
    public List<String> arg;
}
