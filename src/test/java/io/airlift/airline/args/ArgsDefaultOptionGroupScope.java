package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.DefaultOption;
import io.airlift.airline.Option;
import io.airlift.airline.OptionType;

@Command(name = "ArgsDefaultOptionGroupScope")
public class ArgsDefaultOptionGroupScope {

    @Option(name = "--test", arity = 1, required = true, type = OptionType.GROUP)
    @DefaultOption
    public String arg;
}
