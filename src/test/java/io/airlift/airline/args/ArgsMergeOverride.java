package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "ArgsMergeOverride")
public class ArgsMergeOverride extends ArgsMergeAddition {

    /**
     * This is a legal option override because we explicitly stated that override is true
     */
    @Option(name = "--hidden", description = "A now visible option", override = true)
    public boolean hidden = false;
}
