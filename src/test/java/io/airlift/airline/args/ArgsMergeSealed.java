package io.airlift.airline.args;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "ArgsMergeSealed")
public class ArgsMergeSealed extends ArgsMergeParent {

    /**
     * This is a legal option override because we explicitly stated that
     * override is true though we also declare it as sealed which means it can't
     * be further overridden
     */
    @Option(name = "--hidden", description = "A now visible option", override = true, sealed = true)
    private boolean hidden = false;
}
