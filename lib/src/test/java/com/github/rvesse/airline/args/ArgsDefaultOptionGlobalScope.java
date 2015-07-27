package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.OptionType;
import com.github.rvesse.airline.annotations.restrictions.Required;

@Command(name = "ArgsDefaultOptionGlobalScope")
public class ArgsDefaultOptionGlobalScope {

    @Option(name = "--test", arity = 1, type = OptionType.GLOBAL)
    @Required
    @DefaultOption
    public String arg;
}
