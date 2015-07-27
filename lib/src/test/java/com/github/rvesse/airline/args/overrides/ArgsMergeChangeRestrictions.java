package com.github.rvesse.airline.args.overrides;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Unrestricted;

@Command(name = "ArgsMergeChangeRestrictions")
public class ArgsMergeChangeRestrictions extends ArgsMergeInheritRestrictions {

    @Option(name = "--required")
    @Unrestricted
    public String requiredOption;
}
