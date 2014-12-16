package io.airlift.airline;

import java.io.IOException;

import io.airlift.airline.help.Help;
import io.airlift.airline.model.CommandMetadata;

import javax.inject.Inject;

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
