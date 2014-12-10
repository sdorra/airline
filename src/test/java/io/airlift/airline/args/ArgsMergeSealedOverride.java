package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "ArgsMergeSealedOverride")
public class ArgsMergeSealedOverride extends ArgsMergeSealed {

    /**
     * This is an illegal override because the parent option we are trying to override is marked as sealed
     */
    @Option(name = "--hidden", description = "Hidden again", hidden = true, override = true)
    private boolean hidden = false;
}
