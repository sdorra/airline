package com.github.rvesse.airline.examples.shipit;

import java.io.IOException;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.examples.ExampleRunnable;
import com.github.rvesse.airline.help.cli.CliGlobalUsageGenerator;

public class GenerateHelp {

    public static void main(String[] args) {
        Cli<ExampleRunnable> cli = new Cli<ExampleRunnable>(ShipItCli.class);
        
        CliGlobalUsageGenerator<ExampleRunnable> helpGenerator = new CliGlobalUsageGenerator<>();
        try {
            helpGenerator.usage(cli.getMetadata(), System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
