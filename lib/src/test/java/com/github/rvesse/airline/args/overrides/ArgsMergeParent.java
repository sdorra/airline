package com.github.rvesse.airline.args.overrides;

import com.github.rvesse.airline.Option;

public abstract class ArgsMergeParent {

    @Option(name = { "-v", "--verbose" }, description = "Enables verbose hidden")
    public boolean verbose = false;
    
    @Option(name = "--hidden", description = "Hidden option", hidden = true)
    public boolean hidden = false;
}
