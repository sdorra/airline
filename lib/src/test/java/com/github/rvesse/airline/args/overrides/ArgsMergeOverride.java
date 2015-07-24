package com.github.rvesse.airline.args.overrides;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "ArgsMergeOverride")
public class ArgsMergeOverride extends ArgsMergeAddition {

    /**
     * This is a legal option override because we explicitly stated that override is true
     */
    @Option(name = "--hidden", description = "A now visible option", override = true)
    public boolean hidden = false;
}
