package com.github.rvesse.airline.args.overrides;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.Option;

@Command(name = "ArgsMergeNameChange")
public class ArgsMergeNameChange extends ArgsMergeParent {

    /**
     * This is an illegal override because it tries to introduce an additional
     * name for the option
     */
    @Option(name = { "--hidden", "--extra" }, description = "Hidden option", hidden = true, override = true)
    public boolean hidden = false;
}
