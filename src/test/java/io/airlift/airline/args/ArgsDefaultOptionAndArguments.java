package io.airlift.airline.args;

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.DefaultOption;
import io.airlift.airline.Option;

@Command(name = "ArgsDefaultOptionAndArguments")
public class ArgsDefaultOptionAndArguments {

    @Option(name = "--test", arity = 1, required = true)
    @DefaultOption
    public String option;
    
    @Arguments
    public String arg;
}
