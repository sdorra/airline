package com.github.rvesse.airline.examples.cli.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.github.rvesse.airline.Arguments;
import com.github.rvesse.airline.Command;
import com.github.rvesse.airline.examples.ExampleRunnable;
import com.github.rvesse.airline.model.GlobalMetadata;

@Command(name = "help", description = "A command that provides help on other commands")
public class Help implements ExampleRunnable {

    @Inject
    private GlobalMetadata global;

    @Arguments(description = "Provides the name of the commands you want to provide help from")
    private List<String> commandNames = new ArrayList<String>();

    @Override
    public int run() {
        try {
            com.github.rvesse.airline.help.Help.help(global, commandNames);
        } catch (IOException e) {
            System.err.println("Failed to output help: " + e.getMessage());
            e.printStackTrace(System.err);
            return 1;
        }
        return 0;
    }

}
