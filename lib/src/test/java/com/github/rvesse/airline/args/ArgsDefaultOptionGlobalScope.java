package com.github.rvesse.airline.args;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.DefaultOption;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.OptionType;

@Command(name = "ArgsDefaultOptionGlobalScope")
public class ArgsDefaultOptionGlobalScope {

    @Option(name = "--test", arity = 1, required = true, type = OptionType.GLOBAL)
    @DefaultOption
    public String arg;
}
