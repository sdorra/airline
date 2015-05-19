package io.airlift.airline.command;

import java.util.List;

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Command(name = "commandWithGroupNames", description = "A command with a group annotation", groupNames = {"singleGroup","singletonGroup"})
public class CommandWithGroupNames
{
    @Arguments(description = "Patterns of files to be added")
    public List<String> patterns;

    @Option(name = "-i")
    public Boolean interactive = false;
}
