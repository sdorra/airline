package com.github.rvesse.airline;

import java.io.IOException;

import javax.inject.Inject;

import com.github.rvesse.airline.help.CommandUsageGenerator;
import com.github.rvesse.airline.help.cli.CliCommandUsageGenerator;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.google.common.base.Preconditions;

/**
 * An option that provides a simple way for the user to request help with a
 * command
 *
 */
public class HelpOption {
    @Inject
    private GlobalMetadata globalMetadata;

    @Inject
    private CommandGroupMetadata groupMetadata;

    @Inject
    private CommandMetadata commandMetadata;

    @Option(name = { "-h", "--help" }, description = "Display help information")
    public Boolean help = false;

    private boolean shown = false;

    public boolean showHelpIfRequested() {
        return this.showHelpIfRequested(new CliCommandUsageGenerator());
    }

    public boolean showHelpIfRequested(CommandUsageGenerator generator) {
        if (help && !shown) {
            Preconditions.checkNotNull(generator, "Usage generator cannot be null");
            try {
                generator.usage(globalMetadata != null ? globalMetadata.getName() : null,
                        groupMetadata != null ? groupMetadata.getName() : null, commandMetadata.getName(),
                        commandMetadata);
            } catch (IOException e) {
                throw new RuntimeException("Error generating usage documentation", e);
            }
            shown = true;
        }
        return help;
    }
}
