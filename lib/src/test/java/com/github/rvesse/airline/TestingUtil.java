package com.github.rvesse.airline;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.builder.CliBuilder;

public class TestingUtil
{
    public static <T> SingleCommand<T> singleCommandParser(Class<T> commandClass)
    {
        return SingleCommand.singleCommand(commandClass);
    }
    
    public static <T> Cli<T> singleCli(Class<T> commandClass)
    {
        //@formatter:off
        CliBuilder<T> builder = Cli.<T>builder("parser")
                                   .withCommand(commandClass);
        //@formatter:off
        
        return builder.build();
    }
    
    public static <T> Cli<T> singleAbbreviatedCommandParser(Class<T> commandClass)
    {
        //@formatter:off
        CliBuilder<T> builder = Cli.<T>builder("parser")
                                   .withCommand(commandClass);
        builder.withParser()
               .withCommandAbbreviation();
        //@formatter:on
        
        return builder.build();
    }
    
    public static <T> Cli<T> singleAbbreviatedOptionParser(Class<T> commandClass)
    {
        //@formatter:off
        CliBuilder<T> builder = Cli.<T>builder("parser")
                                   .withCommand(commandClass);
        builder.withParser()
               .withOptionAbbreviation();
        //@formatter:on
        
        return builder.build();
    }
}
