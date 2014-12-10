package io.airlift.airline.args;

import io.airlift.airline.Option;

public abstract class ArgsMergeParent {

    @Option(name = { "-v", "--verbose" }, description = "Enables verbose hidden")
    protected boolean verbose = false;
    
    @Option(name = "--hidden", description = "Hidden option", hidden = true)
    private boolean hidden = false;
}
