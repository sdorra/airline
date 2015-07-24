package com.github.rvesse.airline.args.overrides;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "ArgsMergeTypeChange")
public class ArgsMergeInvalidTypeChange extends ArgsMergeTypeParent {

    /**
     * Illegal override since we can't change the effective type of an option to something completely different
     */
    @Option(name = "--string", arity = 1, override = true)
    public double s;
}
