package com.github.rvesse.airline.examples.cli.aliases;

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
 * An example of creating a CLI that takes advantage of the aliases feature.
 * <p>
 * Aliases provide a means by which you can define additional top level commands
 * that simply delegate to actual commands
 * </p>
 *
 */
public class AliasedCli {

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
        // Define an alias
        // Here we define an alias example that invokes the simple command passing in some arguments
        // Any additional arguments an alias is invoked will be passed through normally
        builder.withParser()
               .withAlias("example")
               .withArguments("simple", "--name", "alias");
        
        //@formatter:on

        ExampleExecutor.executeCli(builder.build(), args);
    }

}
