package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.OptionType;

@Command(name = "ArgsDefaultOptionGroupScope")
public class ArgsDefaultOptionGroupScope {

    @Option(name = "--test", arity = 1, required = true, type = OptionType.GROUP)
    @DefaultOption
    public String arg;
}
