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
public class HelpOption<C> {
    @Inject
    private GlobalMetadata<C> globalMetadata;

    @Inject
    private CommandGroupMetadata groupMetadata;

    @Inject
    private CommandMetadata commandMetadata;

    @Option(name = { "-h", "--help" }, description = "Display help information")
    public Boolean help = false;

    private boolean shown = false;

    /**
     * Shows help if user requested it and it hasn't already been shown using
     * the default {@link CliCommandUsageGenerator}
     * 
     * @return True if help was requested by the user
     */
    public boolean showHelpIfRequested() {
        return this.showHelpIfRequested(new CliCommandUsageGenerator());
    }

    /**
     * Shows help if user requested it and it hasn't already been shown
     * 
     * @param generator
     *            Usage generator
     * @return True if help was requested by the user
     */
    public boolean showHelpIfRequested(CommandUsageGenerator generator) {
        if (help && !shown) {
            showHelp(generator);
            shown = true;
        }
        return help;
    }

    /**
     * Shows help using the default {@link CliCommandUsageGenerator}
     */
    public void showHelp() {
        showHelp(new CliCommandUsageGenerator());
    }

    /**
     * Shows help using the given usage generator
     * 
     * @param generator
     *            Usage generator
     */
    public void showHelp(CommandUsageGenerator generator) {
        Preconditions.checkNotNull(generator, "Usage generator cannot be null");
        try {
            generator.usage(globalMetadata != null ? globalMetadata.getName() : null,
                    groupMetadata != null ? groupMetadata.getName() : null, commandMetadata.getName(), commandMetadata);
        } catch (IOException e) {
            throw new RuntimeException("Error generating usage documentation", e);
        }
    }
}
