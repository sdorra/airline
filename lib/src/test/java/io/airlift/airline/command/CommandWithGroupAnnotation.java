package io.airlift.airline.command;

import java.util.List;

import io.airlift.airline.Group;
import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.Option;

@Group(name = "singleGroup", description = "a single group", defaultCommand = CommandWithGroupAnnotation.class,commands = {CommandAdd.class})
@Command(name = "commandWithGroup", description = "A command with a group annotation")
public class CommandWithGroupAnnotation
{
    @Arguments(description = "Patterns of files to be added")
    public List<String> patterns;

    @Option(name = "-i")
    public Boolean interactive = false;
}
