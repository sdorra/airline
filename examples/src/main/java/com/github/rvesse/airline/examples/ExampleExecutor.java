package com.github.rvesse.airline.examples;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.parser.ParseException;

/**
 * Helper class that launches and runs the actual example commands and CLIs
 * 
 * @author rvesse
 *
 */
public class ExampleExecutor {

    private static <T extends ExampleRunnable> void execute(T cmd) {
        try {
            int exitCode = cmd.run();
            System.out.println();
            System.out.println("Exiting with Code " + exitCode);
            System.exit(exitCode);
        } catch (Throwable e) {
            System.err.println("Command threw error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public static <T extends ExampleRunnable> void executeSingleCommand(Class<T> cls, String[] args) {
        SingleCommand<T> parser = SingleCommand.singleCommand(cls);
        try {
            T cmd = parser.parse(args);
            execute(cmd);
        } catch (ParseException e) {
            System.err.println("Parser error: " + e.getMessage());
        } catch (Throwable e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public static <T extends ExampleRunnable> void executeCli(Cli<T> cli, String[] args) {
        try {
            T cmd = cli.parse(args);
            execute(cmd);
        } catch (ParseException e) {
            System.err.println("Parser error: " + e.getMessage());
        } catch (Throwable e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
