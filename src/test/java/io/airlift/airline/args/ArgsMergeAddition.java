package io.airlift.airline.args;

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;

import java.util.ArrayList;
import java.util.List;

@Command(name = "ArgsMergeAddition")
public class ArgsMergeAddition extends ArgsMergeParent {

    @Arguments
    public List<String> args = new ArrayList<>();
}
