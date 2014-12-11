package io.airlift.airline.args.overrides;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "ArgsMergeNarrowingTypeChange")
public class ArgsMergeNarrowingTypeChange extends ArgsMergeTypeParent {

    @Option(name = "--test", arity = 1, override = true)
    public B test;
}
