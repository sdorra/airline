package com.github.rvesse.airline.examples.simple;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.Arguments;
import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.Option;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.ExampleRunnable;

/**
 * A simple example that demonstrates most of the basic concepts
 * <p>
 * We use the {@link Command} annotation to indicate that a class is going to
 * represent a command and then the {@link Option} annotation to indicate fields
 * whose values should be populated by options
 * </p>
 *
 */
@Command(name = "simple", description = "A simple example command")
public class Simple implements ExampleRunnable {

    /**
     * The special {@link HelpOption} provides a {@code -h} and {@code --help}
     * option that can be used to request that help be shown.
     * <p>
     * Developers need to check the {@link HelpOption#showHelpIfRequested()}
     * method which will display help if requested and return {@code true} if
     * the user requested the help
     * </p>
     */
    @Inject
    private HelpOption help;

    @Option(name = { "-f", "--flag" }, description = "An option that requires no arguments")
    private boolean flag = false;

    /**
     * Here we declare an option that requires a value by using the
     * {@code arity} field of the {@link Option} annotation
     */
    @Option(name = { "-n", "--name" }, title = "Name", arity = 1, description = "An option that takes an argument")
    private String name;

    /**
     * As we declare this field to be of type {@code int} Airline will ensure
     * that the value passed to this option can be converted to an integer and
     * throws an error if this is not possible
     */
    @Option(name = { "--number" }, title = "Number", arity = 1, description = "An option that takes a numeric argument")
    private int number;

    /**
     * The {@link Arguments} annotation allows commands to take in additional
     * arbitrary arguments
     */
    @Arguments
    private List<String> args;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(Simple.class, args);
    }

    @Override
    public int run() {
        if (!help.showHelpIfRequested()) {
            System.out.println("Flag was " + (this.flag ? "set" : "not set"));
            System.out.println("Name was " + this.name);
            System.out.println("Number was " + this.number);
            if (args != null)
                System.out.println("Arguments were " + StringUtils.join(args, ","));

        }
        return 0;
    }
}
