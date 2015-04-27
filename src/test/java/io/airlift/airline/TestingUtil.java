package io.airlift.airline;

public class TestingUtil
{
    public static <T> Cli<T> singleCommandParser(Class<T> commandClass)
    {
        return Cli.<T>builder("parser")
                .withCommand(commandClass)
                .build();
    }
    
    public static <T> Cli<T> singleAbbreviatedCommandParser(Class<T> commandClass)
    {
        return Cli.<T>builder("parser")
                .withCommand(commandClass)
                .withCommandAbbreviation()
                .build();
    }
}
