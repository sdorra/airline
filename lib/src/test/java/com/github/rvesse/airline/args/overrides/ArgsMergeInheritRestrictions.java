package com.github.rvesse.airline.args.overrides;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.args.OptionsRequired;

@Command(name = "ArgsMergeInheritRestrictions")
public class ArgsMergeInheritRestrictions extends OptionsRequired {

    @Option(name = "--required")
    public String requiredOption;
}
