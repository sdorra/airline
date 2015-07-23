package com.github.rvesse.airline.command;

import java.util.List;

import com.github.rvesse.airline.*;

@Groups({
        @Group(name = "groupInsideOfGroups", description = "my nested group", defaultCommand = CommandWithGroupsAnnotation.class,commands = {CommandAdd.class})
})
@Command(name = "commandWithGroupsAnno", description = "A command with a groups annotation")
public class CommandWithGroupsAnnotation
{
    @Arguments(description = "Patterns of files to be added")
    public List<String> patterns;

    @Option(name = "-i")
    public Boolean interactive = false;
    
    @Option(name = "-v", type = OptionType.GROUP)
    public boolean verbose = false;
    
}
