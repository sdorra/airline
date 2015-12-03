package com.github.rvesse.airline.examples.userguide;

import com.github.rvesse.airline.annotations.Cli;

//@formatter:off
@Cli(name = "basic", 
    description = "Provides a basic example CLI",
    defaultCommand = GettingStarted.class, 
    commands = { GettingStarted.class, Tool.class })
//@formatter:on
public class BasicCli {

    public static void main(String[] args) {
        com.github.rvesse.airline.Cli<Runnable> cli = new com.github.rvesse.airline.Cli<>(BasicCli.class);
        Runnable cmd = cli.parse(args);
        cmd.run();
    }
}
