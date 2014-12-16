package io.airlift.airline.help.cli;

import static com.google.common.collect.Lists.newArrayList;
import static io.airlift.airline.help.UsageHelper.DEFAULT_OPTION_COMPARATOR;
import static io.airlift.airline.help.UsageHelper.toSynopsisUsage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import io.airlift.airline.help.AbstractPrintedCommandUsageGenerator;
import io.airlift.airline.help.UsageHelper;
import io.airlift.airline.help.UsagePrinter;
import io.airlift.airline.model.ArgumentsMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.OptionMetadata;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;

public class CliCommandUsageGenerator extends AbstractPrintedCommandUsageGenerator {

    public CliCommandUsageGenerator() {
        this(79, DEFAULT_OPTION_COMPARATOR);
    }

    public CliCommandUsageGenerator(int columnSize) {
        this(columnSize, DEFAULT_OPTION_COMPARATOR);
    }

    public CliCommandUsageGenerator(int columnSize, @Nullable Comparator<? super OptionMetadata> optionComparator) {
        super(columnSize, optionComparator);
    }

    @Override
    public void usage(@Nullable String programName, @Nullable String groupName, String commandName,
            CommandMetadata command, UsagePrinter out) throws IOException {
        //
        // NAME
        //
        out.append("NAME").newline();

        out.newIndentedPrinter(8).append(programName).append(groupName).append(commandName).append("-")
                .append(command.getDescription()).newline().newline();

        //
        // SYNOPSIS
        //
        out.append("SYNOPSIS").newline();
        UsagePrinter synopsis = out.newIndentedPrinter(8).newPrinterWithHangingIndent(8);
        List<OptionMetadata> options = newArrayList();
        if (programName != null) {
            synopsis.append(programName).appendWords(toSynopsisUsage(sortOptions(command.getGlobalOptions())));
            options.addAll(command.getGlobalOptions());
        }
        if (groupName != null) {
            synopsis.append(groupName).appendWords(toSynopsisUsage(sortOptions(command.getGroupOptions())));
            options.addAll(command.getGroupOptions());
        }
        synopsis.append(commandName).appendWords(toSynopsisUsage(sortOptions(command.getCommandOptions())));
        options.addAll(command.getCommandOptions());

        // command arguments (optional)
        ArgumentsMetadata arguments = command.getArguments();
        if (arguments != null) {
            synopsis.append("[--]").append(UsageHelper.toUsage(arguments));
        }
        synopsis.newline();
        synopsis.newline();

        //
        // OPTIONS
        //
        if (options.size() > 0 || arguments != null) {
            options = sortOptions(options);

            out.append("OPTIONS").newline();

            for (OptionMetadata option : options) {
                // skip hidden options
                if (option.isHidden()) {
                    continue;
                }

                // option names
                UsagePrinter optionPrinter = out.newIndentedPrinter(8);
                optionPrinter.append(UsageHelper.toDescription(option)).newline();

                // description
                UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
                descriptionPrinter.append(option.getDescription()).newline();

                descriptionPrinter.newline();
            }

            if (arguments != null) {
                // "--" option
                UsagePrinter optionPrinter = out.newIndentedPrinter(8);
                optionPrinter.append("--").newline();

                // description
                UsagePrinter descriptionPrinter = optionPrinter.newIndentedPrinter(4);
                descriptionPrinter
                        .append("This option can be used to separate command-line options from the "
                                + "list of argument, (useful when arguments might be mistaken for command-line options")
                        .newline();
                descriptionPrinter.newline();

                // arguments name(s)
                optionPrinter.append(UsageHelper.toDescription(arguments)).newline();

                // description
                descriptionPrinter.append(arguments.getDescription()).newline();
                descriptionPrinter.newline();
            }
        }

        if (command.getDiscussion() != null) {
            out.append("DISCUSSION").newline();
            UsagePrinter disc = out.newIndentedPrinter(8);

            disc.append(command.getDiscussion()).newline().newline();
        }

        if (command.getExamples() != null && !command.getExamples().isEmpty()) {
            out.append("EXAMPLES").newline();
            UsagePrinter ex = out.newIndentedPrinter(8);

            ex.appendTable(Iterables.partition(command.getExamples(), 1));
        }
    }

}
