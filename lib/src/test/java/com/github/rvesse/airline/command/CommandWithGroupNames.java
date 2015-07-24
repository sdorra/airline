package com.github.rvesse.airline.command;

import java.util.List;

import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;

@Command(name = "commandWithGroupNames", description = "A command with a group annotation", groupNames = {"singleGroup","singletonGroup"})
public class CommandWithGroupNames
{
    @Arguments(description = "Patterns of files to be added")
    public List<String> patterns;

    @Option(name = "-i")
    public Boolean interactive = false;
}
