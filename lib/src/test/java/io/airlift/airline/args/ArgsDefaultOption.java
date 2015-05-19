package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.DefaultOption;
import io.airlift.airline.Option;

@Command(name = "ArgsDefaultOption")
public class ArgsDefaultOption {

    @Option(name = "--test", arity = 1, required = true)
    @DefaultOption
    public String arg;
}
