package com.github.rvesse.airline.examples.modules;

import javax.inject.Inject;

import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;

/**
 * If you have some set of options that make sense together you can modularize
 * them out as a class and inject them into your actual command class
 * <p>
 * This is particularly useful if you want to avoid using inheritance for
 * options, especially in cases where different commands may use different
 * combinations of some common option sets
 * </p>
 *
 */
@Command(name = "modules", description = "A command that demonstrates the use of modules to group together sets of options for composition and reuse")
public class Modules implements ExampleRunnable {

    @Inject
    private HelpOption help;

    @Inject
    public CredentialsModule credentials = new CredentialsModule();

    @Inject
    public VerbosityModule verbosity = new VerbosityModule();

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(Modules.class, args);
    }

    @Override
    public int run() {
        if (!help.showHelpIfRequested()) {
            switch (verbosity.verbosity) {
            case 3:
                System.out.println("Password is " + credentials.password);
            case 2:
                System.out.println("Verbosity set to " + verbosity.verbosity);
            case 1:
                System.out.println("User is " + credentials.user);
                break;
            default:
                System.err.println("Unexpected verbosity");
            }
        }
        return 0;
    }
}
