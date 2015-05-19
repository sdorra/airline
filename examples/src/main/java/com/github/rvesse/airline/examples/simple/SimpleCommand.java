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

@Command(name = "simple", description = "A simple example command")
public class SimpleCommand implements ExampleRunnable {

    @Inject
    private HelpOption help;

    @Option(name = { "-f", "--flag" }, description = "An option that requires no arguments")
    private boolean flag = false;

    @Option(name = { "-n", "--name" }, title = "Name", arity = 1, description = "An option that takes an argument")
    private String name;

    @Option(name = { "--number" }, title = "Number", arity = 1, description = "An option that takes a numeric argument")
    private int number;

    @Arguments
    private List<String> args;

    public static void main(String[] args) {
        ExampleExecutor.executeSingleCommand(SimpleCommand.class, args);
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
