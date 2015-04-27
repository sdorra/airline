package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.DefaultOption;
import io.airlift.airline.Option;

@Command(name = "ArgsMultipleDefaultOption")
public class ArgsMultipleDefaultOptions {

    @Option(name = "-a", arity = 1, required = true)
    @DefaultOption
    public String arg1;
    
    @Option(name = "-b", arity = 1, required = true)
    @DefaultOption
    public String arg2;
}
