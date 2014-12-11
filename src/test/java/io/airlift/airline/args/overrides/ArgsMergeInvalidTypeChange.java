package io.airlift.airline.args.overrides;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "ArgsMergeTypeChange")
public class ArgsMergeInvalidTypeChange extends ArgsMergeTypeParent {

    /**
     * Illegal override since we can't change the effective type of an option
     */
    @Option(name = "--string", arity = 1, override = true)
    public double s;
}
