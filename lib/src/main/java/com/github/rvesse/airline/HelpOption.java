package com.github.rvesse.airline;

import java.io.IOException;

import javax.inject.Inject;

import com.github.rvesse.airline.help.Help;
import com.github.rvesse.airline.model.CommandMetadata;

public class HelpOption
{
    @Inject
    public CommandMetadata commandMetadata;

    @Option(name = {"-h", "--help"}, description = "Display help information")
    public Boolean help = false;

    public boolean showHelpIfRequested()
    {
        if (help) {
            try {
                Help.help(commandMetadata);
            } catch (IOException e) {
                throw new RuntimeException("Error generating usage documentation", e);
            }
        }
        return help;
    }
}
