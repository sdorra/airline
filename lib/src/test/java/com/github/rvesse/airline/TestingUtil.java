package com.github.rvesse.airline;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.builder.CliBuilder;

public class TestingUtil
{
    public static <T> Cli<T> singleCommandParser(Class<T> commandClass)
    {
        //@formatter:off
        return Cli.<T>builder("parser")
                  .withCommand(commandClass)
                  .build();
        //@formatter:on
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
