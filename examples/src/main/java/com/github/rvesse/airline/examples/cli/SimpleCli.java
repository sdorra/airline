package com.github.rvesse.airline.examples.cli;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;
import com.github.rvesse.airline.examples.cli.commands.Help;
import com.github.rvesse.airline.examples.inheritance.Child;
import com.github.rvesse.airline.examples.inheritance.GoodGrandchild;
import com.github.rvesse.airline.examples.inheritance.Parent;
import com.github.rvesse.airline.examples.simple.Simple;

/**
 * A simple example of creating a CLI
 *
 */
public class SimpleCli {
    
    public static void main(String[] args) {
        //@formatter:off
        @SuppressWarnings("unchecked")
        // The program name is cli
        CliBuilder<ExampleRunnable> builder = Cli.<ExampleRunnable>builder("cli")
                                                 // Add a description
                                                 .withDescription("A simple CLI with several commands available")
                                                 // Define some commands
                                                 .withCommand(Simple.class)
                                                 .withCommands(Parent.class, Child.class, GoodGrandchild.class)
                                                 .withCommand(Help.class);
        //@formatter:on
        
        ExampleExecutor.executeCli(builder.build(), args);
    }

}
