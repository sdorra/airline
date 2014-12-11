package io.airlift.airline.args.overrides;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "ArgsMergeNameChange")
public class ArgsMergeNameChange extends ArgsMergeParent {

    /**
     * This is an illegal override because it tries to introduce an additional
     * name for the option
     */
    @Option(name = { "--hidden", "--extra" }, description = "Hidden option", hidden = true, override = true)
    public boolean hidden = false;
}
