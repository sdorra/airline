package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.DefaultOption;
import io.airlift.airline.Option;
import io.airlift.airline.OptionType;

@Command(name = "ArgsDefaultOptionGlobalScope")
public class ArgsDefaultOptionGlobalScope {

    @Option(name = "--test", arity = 1, required = true, type = OptionType.GLOBAL)
    @DefaultOption
    public String arg;
}
