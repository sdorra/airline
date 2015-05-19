package com.github.rvesse.airline.args.overrides;

import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.Arguments;
import com.github.rvesse.airline.Command;

@Command(name = "ArgsMergeAddition")
public class ArgsMergeAddition extends ArgsMergeParent {

    @Arguments
    public List<String> args = new ArrayList<>();
}
