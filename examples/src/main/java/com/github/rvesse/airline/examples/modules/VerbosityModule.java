package com.github.rvesse.airline.examples.modules;

import com.github.rvesse.airline.Option;

public class VerbosityModule {

    @Option(name = { "-v", "--verbosity" }, arity = 1, title = "Level", allowedValues = { "1", "2", "3" }, description = "Sets the desired verbosity")
    public int verbosity = 1;
}
