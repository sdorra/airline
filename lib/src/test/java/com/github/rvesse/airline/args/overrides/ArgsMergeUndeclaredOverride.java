package com.github.rvesse.airline.args.overrides;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;

@Command(name = "ArgsMergeOverride")
public class ArgsMergeUndeclaredOverride extends ArgsMergeAddition {

    /**
     * This is an illegal override because we failed to specify this is an override
     */
    @Option(name = "--hidden", description = "A now visible option")
    private boolean hidden = false;
}
