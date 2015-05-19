package io.airlift.airline.args.overrides;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "ArgsMergeWideningTypeChange")
public class ArgsMergeWideningTypeChange extends ArgsMergeTypeParent {

    /**
     * This is an illegal override as it involves a widening type conversion
     */
    @Option(name = "--test", arity = 1, override = true)
    public A test;
}
