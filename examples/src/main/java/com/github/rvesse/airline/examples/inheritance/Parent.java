package com.github.rvesse.airline.examples.inheritance;

import javax.inject.Inject;

import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;

/**
 * We can use standard Java inheritance with commands and the child commands
 * will inherit options defined on their parents
 */
@Command(name = "parent", description = "A parent command")
public class Parent implements ExampleRunnable {

    @Inject
    protected HelpOption help;

    @Option(name = "--parent", description = "An option provided by the parent")
    private boolean parent;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(Parent.class, args);
    }

    @Override
    public int run() {
        if (!help.showHelpIfRequested()) {
            System.out.println("--parent was " + (this.parent ? "set" : "not set"));
        }
        return 0;
    }

}
