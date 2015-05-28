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
 * that simply delegate to actual commands. In this example we levarage the more
 * advanced positional parameters feature to allow our alias to be passed simple
 * arguments and invoke complex arguments on the actual command.
 * </p>
 *
 */
public class PositionalAliasedCli {

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

        // Here we define an alias example that invokes the simple command and
        // uses positional parameters. These are a 1 based index of the
        // parameters that a user passes to the alias and allows alias
        // parameters to be constructed into more complex command invocations
        //
        // Syntax is $1
        //
        // If a positional parameter references a parameter that is not provided
        // to the alias the parameter is passed as-is
        //
        // In this example the first parameter that the user passes to the alias
        // is passed as the value of the --name option to the simple command
        //
        // Any additional arguments an alias is invoked will be passed through
        // normally

        // :formatter:off
        builder.withAlias("example").withArguments("simple", "--name", "$1");
        // @formatter:on

        ExampleExecutor.executeCli(builder.build(), args);
    }

}
