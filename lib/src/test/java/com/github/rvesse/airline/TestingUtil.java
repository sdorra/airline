package com.github.rvesse.airline;

import com.github.rvesse.airline.Cli;

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
        return Cli.<T>builder("parser")
                  .withCommand(commandClass)
                  .withCommandAbbreviation()
                  .build();
        //@formatter:on
    }
    
    public static <T> Cli<T> singleAbbreviatedOptionParser(Class<T> commandClass)
    {
        //@formatter:off
        return Cli.<T>builder("parser")
                  .withCommand(commandClass)
                  .withOptionAbbreviation()
                  .build();
        //@formatter:on
    }
}
