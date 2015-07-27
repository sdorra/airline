package com.github.rvesse.airline.args;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.DefaultOption;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.OptionType;
import com.github.rvesse.airline.annotations.restrictions.Required;

@Command(name = "ArgsDefaultOptionGroupScope")
public class ArgsDefaultOptionGroupScope {

    @Option(name = "--test", arity = 1, type = OptionType.GROUP)
    @Required
    @DefaultOption
    public String arg;
}
