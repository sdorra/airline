package io.airlift.airline.help.cli;

import io.airlift.airline.help.AbstractPrintedGlobalUsageGenerator;
import io.airlift.airline.help.UsageHelper;
import io.airlift.airline.help.UsagePrinter;
import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;
import io.airlift.airline.model.OptionMetadata;

import javax.annotation.Nullable;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static io.airlift.airline.help.UsageHelper.DEFAULT_OPTION_COMPARATOR;

public class CliGlobalUsageGenerator extends AbstractPrintedGlobalUsageGenerator {
    public CliGlobalUsageGenerator() {
        this(79);
    }

    public CliGlobalUsageGenerator(int columnSize) {
        this(columnSize, DEFAULT_OPTION_COMPARATOR, UsageHelper.DEFAULT_COMMAND_COMPARATOR, UsageHelper.DEFAULT_COMMAND_GROUP_COMPARATOR);
    }

    public CliGlobalUsageGenerator(int columnSize, @Nullable Comparator<? super OptionMetadata> optionComparator,
            @Nullable Comparator<? super CommandMetadata> commandComparator,
            @Nullable Comparator<? super CommandGroupMetadata> commandGroupComparator) {
        super(columnSize, optionComparator, commandComparator, commandGroupComparator);
    }

    @Override
    protected void usage(GlobalMetadata global, UsagePrinter out) throws IOException {
        //
        // NAME
        //
        out.append("NAME").newline();

        out.newIndentedPrinter(8).append(global.getName()).append("-").append(global.getDescription()).newline()
                .newline();

        //
        // SYNOPSIS
        //
        out.append("SYNOPSIS").newline();
        out.newIndentedPrinter(8).newPrinterWithHangingIndent(8).append(global.getName())
                .appendWords(toSynopsisUsage(global.getOptions())).append("<command> [ <args> ]").newline().newline();

        //
        // OPTIONS
        //
        List<OptionMetadata> options = sortOptions(global.getOptions());
        if (options.size() > 0) {

            out.append("OPTIONS").newline();

            for (OptionMetadata option : options) {

                if (option.isHidden()) {
                    continue;
                }

                // option names
                UsagePrinter optionPrinter = out.newIndentedPrinter(8);
                optionPrinter.append(toDescription(option)).newline();

                // description
                UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
                descriptionPrinter.append(option.getDescription()).newline();

                descriptionPrinter.newline();
            }
        }

        //
        // COMMANDS
        //
        out.append("COMMANDS").newline();
        UsagePrinter commandPrinter = out.newIndentedPrinter(8);

        for (CommandMetadata command : sortCommands(global.getDefaultGroupCommands())) {
            printCommandDescription(commandPrinter, null, command);
        }
        for (CommandGroupMetadata group : sortCommandGroups(global.getCommandGroups())) {
            for (CommandMetadata command : sortCommands(group.getCommands())) {
                printCommandDescription(commandPrinter, group, command);
            }
        }
    }

    /**
     * Prints the description for a command
     * 
     * @param commandPrinter
     *            Usage printer
     * @param group
     *            Group metadata
     * @param command
     *            Command metadata
     * @throws IOException
     */
    protected void printCommandDescription(UsagePrinter commandPrinter, @Nullable CommandGroupMetadata group,
            CommandMetadata command) throws IOException {
        if (!command.isHidden()) {
            if (group != null) {
                commandPrinter.append(group.getName());
            }
            commandPrinter.append(command.getName()).newline();
            if (command.getDescription() != null) {
                commandPrinter.newIndentedPrinter(4).append(command.getDescription()).newline();
            }
            commandPrinter.newline();
        }
    }
}
