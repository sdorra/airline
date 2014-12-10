package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "ArgsMergeOverride")
public class ArgsMergeOverride extends ArgsMergeAddition {

    @Option(name = "--hidden", description = "A now visible option")
    private boolean hidden = false;
}
