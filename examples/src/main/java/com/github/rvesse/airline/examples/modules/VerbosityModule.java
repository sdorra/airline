package com.github.rvesse.airline.examples.modules;

import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.AllowedRawValues;

public class VerbosityModule {

    @Option(name = { "-v", "--verbosity" }, arity = 1, title = "Level", description = "Sets the desired verbosity")
    // The AllowedValues annotation allows an option to be restricted to a given set of values
    @AllowedRawValues(allowedValues = { "1", "2", "3" })
    public int verbosity = 1;
}
