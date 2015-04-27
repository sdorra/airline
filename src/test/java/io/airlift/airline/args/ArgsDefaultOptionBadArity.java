package io.airlift.airline.args;

import java.util.List;

import io.airlift.airline.Command;
import io.airlift.airline.DefaultOption;
import io.airlift.airline.Option;

@Command(name = "ArgsDefaultOption")
public class ArgsDefaultOptionBadArity {

    @Option(name = "--test", arity = 2, required = true)
    @DefaultOption
    public List<String> arg;
}
