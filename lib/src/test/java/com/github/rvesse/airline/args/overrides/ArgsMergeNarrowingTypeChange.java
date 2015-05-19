package com.github.rvesse.airline.args.overrides;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;

@Command(name = "ArgsMergeNarrowingTypeChange")
public class ArgsMergeNarrowingTypeChange extends ArgsMergeTypeParent {

    @Option(name = "--test", arity = 1, override = true)
    public C test;
}
