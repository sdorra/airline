package com.github.rvesse.airline.args;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.DefaultOption;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.OptionType;

@Command(name = "ArgsDefaultOptionGroupScope")
public class ArgsDefaultOptionGroupScope {

    @Option(name = "--test", arity = 1, required = true, type = OptionType.GROUP)
    @DefaultOption
    public String arg;
}
